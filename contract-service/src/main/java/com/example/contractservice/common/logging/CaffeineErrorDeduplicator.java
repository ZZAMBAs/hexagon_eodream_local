package com.example.contractservice.common.logging;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

public class CaffeineErrorDeduplicator implements SlackErrorDeduplicator {
    private Cache<String, Boolean> cache;

    public CaffeineErrorDeduplicator(int ttlMillis) {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(ttlMillis, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public void add(String key) {
        cache.put(key, true);
    }

    @Override
    public boolean isDuplicate(String key) {
        return cache.getIfPresent(key) != null;
    }

    @Override
    public void remove(String key) {
        cache.invalidate(key);
    }
}
