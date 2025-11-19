package com.example.memberservice.common.kafka.producer;

import com.example.memberservice.common.kafka.event.KafkaEvent;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Slf4j
public abstract class AbstractKafkaEventProducer<T> implements EventProducer<T> {

    protected final KafkaTemplate<String, T> kafkaTemplate;
    protected final String topicName;

    protected AbstractKafkaEventProducer(KafkaTemplate<String, T> kafkaTemplate, String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    /**
     * 상속받는 클래스가 지정할 Kafka 토픽 이름
     */

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public CompletableFuture<SendResult<String, T>> sendEvent(KafkaEvent<T> event) {
        log.info("Sending Kafka event [{}] with key={}", event.getClass().getSimpleName(), event.getEventKey());
        return kafkaTemplate.send(
            topicName,      // key와 payload는 이벤트에서 가져옴
            event.getEventKey(),
            event.toEventData()
        );
    }
}
