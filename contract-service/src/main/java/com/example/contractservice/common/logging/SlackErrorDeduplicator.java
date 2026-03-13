package com.example.contractservice.common.logging;

public interface SlackErrorDeduplicator {
    boolean acquire(String key);
    void remove(String key);
}
