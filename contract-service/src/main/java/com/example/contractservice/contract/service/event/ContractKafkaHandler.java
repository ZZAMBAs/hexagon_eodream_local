package com.example.contractservice.contract.service.event;

import com.example.contractservice.contract.service.ContractEventService;
import com.example.contractservice.contract.service.dto.event.CartItemDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(
        topics = {
                "${kafka.consumer.topic.cart.name}",
        }
)
@RequiredArgsConstructor
public class ContractKafkaHandler {
    private final ContractEventService contractEventService;

    @KafkaHandler
    public void handleCartEvent(@Payload CartItemDeletedEvent event) {
        contractEventService.cancelContract(event.code());
    }
}
