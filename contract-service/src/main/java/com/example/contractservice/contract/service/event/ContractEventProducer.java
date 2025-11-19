package com.example.contractservice.contract.service.event;

import com.example.contractservice.contract.service.dto.event.ContractEvent;
import java.util.concurrent.CompletableFuture;
import org.springframework.kafka.support.SendResult;

public interface ContractEventProducer {
    CompletableFuture<SendResult<String, Object>> sendEvent(ContractEvent event);
}
