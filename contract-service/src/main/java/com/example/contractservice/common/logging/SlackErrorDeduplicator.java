package com.example.contractservice.common.logging;

public interface SlackErrorDeduplicator {
    void add(String key);
    boolean isDuplicate(String key);
    void remove(String key);
}
