package com.cache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LRUCacheTest {

    @Test
    void putAndGet() {
        try (LRUCache<String, String> cache = new LRUCache<>(10)) {
            cache.put("a", "1");
            assertEquals("1", cache.get("a"));
            assertEquals(1, cache.size());
            assertEquals(1, cache.getHits());
            assertEquals(0, cache.getMisses());
        }
    }

    @Test
    void getMissIncrementsMisses() {
        try (LRUCache<String, String> cache = new LRUCache<>(10)) {
            assertNull(cache.get("missing"));
            assertEquals(0, cache.getHits());
            assertEquals(1, cache.getMisses());
        }
    }

    @Test
    void overwriteUpdatesValueAndOrder() {
        try (LRUCache<String, String> cache = new LRUCache<>(2)) {
            cache.put("a", "1");
            cache.put("b", "2");
            cache.put("a", "3");
            assertEquals("3", cache.get("a"));
            cache.put("c", "4");
            assertNull(cache.get("b"));
            assertEquals("3", cache.get("a"));
        }
    }

    @Test
    void ttlExpiresLazilyOnGet() throws InterruptedException {
        try (LRUCache<String, String> cache = new LRUCache<>(10, 200, TimeUnit.MILLISECONDS)) {
            cache.put("k", "v", 50);
            Thread.sleep(80);
            assertNull(cache.get("k"));
            assertEquals(0, cache.size());
        }
    }

    @Test
    void ttlStillValid() {
        try (LRUCache<String, String> cache = new LRUCache<>(10)) {
            cache.put("k", "v", 10_000);
            assertEquals("v", cache.get("k"));
        }
    }

    @Test
    void evictionWhenOverCapacity() {
        try (LRUCache<Integer, String> cache = new LRUCache<>(3)) {
            cache.put(1, "a");
            cache.put(2, "b");
            cache.put(3, "c");
            cache.put(4, "d");
            assertNull(cache.get(1));
            assertEquals("d", cache.get(4));
            assertEquals(3, cache.size());
        }
    }

    @Test
    void lruEvictsLeastRecentlyUsed() {
        try (LRUCache<String, String> cache = new LRUCache<>(3)) {
            cache.put("a", "1");
            cache.put("b", "2");
            cache.put("c", "3");
            cache.get("a");
            cache.put("d", "4");
            assertNull(cache.get("b"));
            assertEquals("1", cache.get("a"));
        }
    }

    @Test
    void removeAndClear() {
        try (LRUCache<String, String> cache = new LRUCache<>(10)) {
            cache.put("x", "y");
            assertTrue(cache.remove("x"));
            assertFalse(cache.remove("x"));
            cache.put("a", "1");
            cache.clear();
            assertEquals(0, cache.size());
            assertNull(cache.get("a"));
        }
    }

    @Test
    void nullKeyRejected() {
        try (LRUCache<String, String> cache = new LRUCache<>(10)) {
            assertThrows(NullPointerException.class, () -> cache.put(null, "v"));
            assertThrows(NullPointerException.class, () -> cache.put(null, "v", 100));
            assertThrows(NullPointerException.class, () -> cache.get(null));
            assertThrows(NullPointerException.class, () -> cache.remove(null));
        }
    }

    @Test
    void nonPositiveTtlRejected() {
        try (LRUCache<String, String> cache = new LRUCache<>(10)) {
            assertThrows(IllegalArgumentException.class, () -> cache.put("k", "v", 0));
            assertThrows(IllegalArgumentException.class, () -> cache.put("k", "v", -1));
        }
    }

    @Test
    void capacityZeroStoresNothing() {
        try (LRUCache<String, String> cache = new LRUCache<>(0)) {
            cache.put("a", "1");
            assertEquals(0, cache.size());
            assertNull(cache.get("a"));
        }
    }

    @Test
    void hitRate() {
        try (LRUCache<String, String> cache = new LRUCache<>(10)) {
            assertEquals(0.0, cache.hitRate());
            cache.put("k", "v");
            cache.get("k");
            assertEquals(1.0, cache.hitRate());
            cache.get("missing");
            assertEquals(0.5, cache.hitRate());
        }
    }

    @Test
    @Timeout(10)
    void backgroundCleanupRemovesExpired() throws InterruptedException {
        try (LRUCache<String, String> cache = new LRUCache<>(10, 100, TimeUnit.MILLISECONDS)) {
            cache.put("k", "v", 30);
            Thread.sleep(250);
            assertEquals(0, cache.size());
        }
    }
}
