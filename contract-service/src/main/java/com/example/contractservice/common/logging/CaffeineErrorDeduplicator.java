package com.example.contractservice.common.logging;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

public class CaffeineErrorDeduplicator implements SlackErrorDeduplicator {
    private final Cache<String, Boolean> cache;

    public CaffeineErrorDeduplicator(int ttlMillis) {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(ttlMillis, TimeUnit.MILLISECONDS)
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
