package com.example.memberservice.common.kafka.producer;

import com.example.memberservice.common.kafka.event.KafkaEvent;
import java.util.concurrent.CompletableFuture;
import org.springframework.kafka.support.SendResult;

public interface EventProducer<T> {
    CompletableFuture<SendResult<String, T>> sendEvent(KafkaEvent<T> event);
}
