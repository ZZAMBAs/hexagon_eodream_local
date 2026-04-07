package com.example.contractservice.common.logging;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

public class CaffeineErrorDeduplicator implements SlackErrorDeduplicator {

    private static final int MAXIMUM_CACHE_SIZE = 1000;
    private final Cache<String, Boolean> cache;

    public CaffeineErrorDeduplicator(int ttlMillis) {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(ttlMillis, TimeUnit.MILLISECONDS)
                .maximumSize(MAXIMUM_CACHE_SIZE)
                .build();
    }

    @Override
    public boolean acquire(String key) {
        return cache.asMap().putIfAbsent(key, Boolean.TRUE) == null;
    }

    @Override
    public void remove(String key) {
        cache.invalidate(key);
    }
}
