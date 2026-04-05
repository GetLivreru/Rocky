package com.cache;

/**
 * Holds a cached value and absolute expiration time in milliseconds since epoch.
 * {@link Long#MAX_VALUE} means no expiration.
 *
 * @param <V> value type
 */
public class CacheEntry<V> {

    V value;
    long expireAt;

    public CacheEntry(V value, long expireAt) {
        this.value = value;
        this.expireAt = expireAt;
    }
}
