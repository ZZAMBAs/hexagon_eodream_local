package com.example.profileservice.common.model.vo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, Object event) {
        log.info("Sending event to topic: {} with data: {}", topic, event);

        // 키는 이벤트 데이터의 고유 코드(예: promotionCode)를 사용하거나 null로 보낼 수 있습니다.
        // 여기서는 메시지의 일관된 처리를 위해 null 키를 사용
        kafkaTemplate.send(topic, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.debug("Event sent successfully. Topic: {}, Offset: {}", topic,
                                result.getRecordMetadata().topic(), result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send event to topic: {}. Error: {}", topic, ex.getMessage());
                    }
                });
    }
}
