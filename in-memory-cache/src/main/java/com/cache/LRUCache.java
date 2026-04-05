package com.cache;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * In-memory LRU cache with optional TTL, lazy and background expiration, and statistics.
 *
 * <p><b>Edge cases (contract):</b>
 * <ul>
 *   <li><b>Null keys</b> — not allowed; {@link NullPointerException}.</li>
 *   <li><b>{@code ttlMillis <= 0}</b> in {@link #put(Object, Object, long)} — {@link IllegalArgumentException}.</li>
 *   <li><b>{@code capacity == 0}</b> — no entries are retained; each put evicts immediately.</li>
 * </ul>
 */
public class LRUCache<K, V> implements Cache<K, V>, CacheStatistics, AutoCloseable {

    private static final long NO_EXPIRATION = Long.MAX_VALUE;

    private static final Logger LOG = Logger.getLogger(LRUCache.class.getName());

    private final int capacity;
    private final HashMap<K, Node<K, V>> index;
    private final Node<K, V> head;
    private final Node<K, V> tail;
    private final ReentrantLock lock = new ReentrantLock();
    private final AtomicLong hits = new AtomicLong();
    private final AtomicLong misses = new AtomicLong();
    private final ScheduledExecutorService cleanupScheduler;

    /**
     * @param capacity      maximum entries ({@code >= 0})
     * @param cleanupPeriod cleanup interval for background expiration; must be {@code > 0}
     * @param cleanupUnit   time unit for {@code cleanupPeriod}
     */
    public LRUCache(int capacity, long cleanupPeriod, TimeUnit cleanupUnit) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity must be >= 0");
        }
        if (cleanupPeriod <= 0) {
            throw new IllegalArgumentException("cleanupPeriod must be > 0");
        }
        Objects.requireNonNull(cleanupUnit, "cleanupUnit");
        this.capacity = capacity;
        this.index = new HashMap<>();
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;

        this.cleanupScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "lru-cache-bg-cleanup");
            t.setDaemon(true);
            return t;
        });
        this.cleanupScheduler.scheduleAtFixedRate(
                this::removeExpiredEntriesBackground,
                cleanupPeriod,
                cleanupPeriod,
                cleanupUnit
        );
    }

    /**
     * Same as {@link #LRUCache(int, long, TimeUnit)} with background cleanup every 60 seconds.
     */
    public LRUCache(int capacity) {
        this(capacity, 60, TimeUnit.SECONDS);
    }

    private static boolean isExpired(CacheEntry<?> entry, long now) {
        return entry.expireAt != NO_EXPIRATION && now >= entry.expireAt;
    }

    private void unlink(Node<K, V> n) {
        n.prev.next = n.next;
        n.next.prev = n.prev;
        n.prev = null;
        n.next = null;
    }

    private void addToMru(Node<K, V> n) {
        Node<K, V> last = tail.prev;
        last.next = n;
        n.prev = last;
        n.next = tail;
        tail.prev = n;
    }

    private void moveToMru(Node<K, V> n) {
        if (n.next == tail) {
            return;
        }
        unlink(n);
        addToMru(n);
    }

    private Node<K, V> evictLru() {
        Node<K, V> victim = head.next;
        if (victim == tail) {
            return null;
        }
        unlink(victim);
        index.remove(victim.key);
        LOG.log(Level.FINE, "LRU eviction: key={0}", victim.key);
        return victim;
    }

    private void removeExpiredEntriesBackground() {
        lock.lock();
        try {
            long now = System.currentTimeMillis();
            Node<K, V> cur = head.next;
            while (cur != tail) {
                Node<K, V> next = cur.next;
                if (isExpired(cur.entry, now)) {
                    unlink(cur);
                    index.remove(cur.key);
                    LOG.log(Level.FINE, "Background expiration: key={0}", cur.key);
                }
                cur = next;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        Objects.requireNonNull(key, "key");
        lock.lock();
        try {
            putInternal(key, new CacheEntry<>(value, NO_EXPIRATION));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(K key, V value, long ttlMillis) {
        Objects.requireNonNull(key, "key");
        if (ttlMillis <= 0) {
            throw new IllegalArgumentException("ttlMillis must be > 0");
        }
        lock.lock();
        try {
            long expireAt = System.currentTimeMillis() + ttlMillis;
            putInternal(key, new CacheEntry<>(value, expireAt));
        } finally {
            lock.unlock();
        }
    }

    private void putInternal(K key, CacheEntry<V> entry) {
        Node<K, V> existing = index.get(key);
        if (existing != null) {
            existing.entry = entry;
            moveToMru(existing);
            return;
        }
        while (capacity > 0 && index.size() >= capacity) {
            evictLru();
        }
        if (capacity == 0) {
            return;
        }
        Node<K, V> node = new Node<>(key, entry);
        index.put(key, node);
        addToMru(node);
    }

    @Override
    public V get(K key) {
        Objects.requireNonNull(key, "key");
        lock.lock();
        try {
            Node<K, V> node = index.get(key);
            if (node == null) {
                misses.incrementAndGet();
                return null;
            }
            long now = System.currentTimeMillis();
            if (isExpired(node.entry, now)) {
                unlink(node);
                index.remove(key);
                misses.incrementAndGet();
                LOG.log(Level.FINE, "Lazy expiration on get: key={0}", key);
                return null;
            }
            moveToMru(node);
            hits.incrementAndGet();
            return node.entry.value;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(K key) {
        Objects.requireNonNull(key, "key");
        lock.lock();
        try {
            Node<K, V> node = index.remove(key);
            if (node == null) {
                return false;
            }
            unlink(node);
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        lock.lock();
        try {
            return index.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            index.clear();
            head.next = tail;
            tail.prev = head;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long getHits() {
        return hits.get();
    }

    @Override
    public long getMisses() {
        return misses.get();
    }

    @Override
    public double hitRate() {
        long h = hits.get();
        long m = misses.get();
        long total = h + m;
        return total == 0 ? 0.0 : (double) h / total;
    }

    /**
     * Stops the background cleanup thread. The cache must not be used after closing.
     */
    @Override
    public void close() {
        cleanupScheduler.shutdown();
        try {
            if (!cleanupScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupScheduler.shutdownNow();
            Thread.currentThread().interrupt();
            LOG.log(Level.WARNING, "Interrupted while shutting down cleanup scheduler", e);
        }
    }

    private static final class Node<K, V> {
        final K key;
        CacheEntry<V> entry;
        Node<K, V> prev;
        Node<K, V> next;

        Node(K key, CacheEntry<V> entry) {
            this.key = key;
            this.entry = entry;
        }
    }

}
