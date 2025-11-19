package com.example.memberservice.common.kafka.event;

public interface KafkaEvent<T> {
    T toEventData();

    String getEventKey();
}
