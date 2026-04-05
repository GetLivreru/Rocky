package com.cache;

/**
 * Hit/miss counters for a cache.
 */
public interface CacheStatistics {

    long getHits();

    long getMisses();

    /**
     * @return {@code hits / (hits + misses)}, or {@code 0.0} if there were no lookups
     */
    double hitRate();
}
