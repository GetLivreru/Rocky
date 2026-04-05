package com.cache;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LRUCacheConcurrencyTest {

    private static final int THREADS = 10;
    private static final int OPS_PER_THREAD = 200;

    @Test
    void manyThreadsPutGetNoErrorsBoundedSize() throws Exception {
        try (LRUCache<Integer, Integer> cache = new LRUCache<>(50, 1, TimeUnit.SECONDS)) {
            ExecutorService pool = Executors.newFixedThreadPool(THREADS);
            List<Future<?>> futures = new ArrayList<>();
            AtomicInteger errors = new AtomicInteger();

            for (int t = 0; t < THREADS; t++) {
                final int tid = t;
                futures.add(pool.submit((Callable<Void>) () -> {
                    try {
                        for (int i = 0; i < OPS_PER_THREAD; i++) {
                            int key = (tid * OPS_PER_THREAD + i) % 100;
                            cache.put(key, i);
                            cache.get(key);
                            if (i % 20 == 0) {
                                cache.remove(key);
                            }
                        }
                    } catch (RuntimeException e) {
                        errors.incrementAndGet();
                        throw e;
                    }
                    return null;
                }));
            }

            for (Future<?> f : futures) {
                f.get();
            }
            pool.shutdown();
            assertTrue(pool.awaitTermination(30, TimeUnit.SECONDS));

            assertEquals(0, errors.get());
            assertTrue(cache.size() <= 50);
            assertEquals(THREADS * OPS_PER_THREAD, cache.getHits() + cache.getMisses());
        }
    }
}
