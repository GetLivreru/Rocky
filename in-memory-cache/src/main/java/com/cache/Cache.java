package com.cache;

/**
 * Thread-safe in-memory cache contract with LRU eviction and optional per-entry TTL.
 *
 * @param <K> key type — must not be {@code null} (implementations typically reject null keys)
 * @param <V> value type
 */
public interface Cache<K, V> {

    void put(K key, V value);

    /**
     * Associates value with key and a time-to-live.
     *
     * @param ttlMillis strictly positive milliseconds until expiration; behavior for non-positive values is defined by the implementation
     */
    void put(K key, V value, long ttlMillis);

    /**
     * Returns the value for {@code key}, or {@code null} if missing or expired.
     */
    V get(K key);

    /**
     * @return {@code true} if an entry was removed
     */
    boolean remove(K key);

    int size();

    void clear();
}
