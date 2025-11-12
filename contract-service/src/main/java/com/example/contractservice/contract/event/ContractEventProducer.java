package com.example.contractservice.contract.event;

import com.example.contractservice.contract.event.dto.ContractConfirmEvent;
import java.util.concurrent.CompletableFuture;
import org.springframework.kafka.support.SendResult;

public interface ContractEventProducer {
    CompletableFuture<SendResult<String, Object>> sendConfirmEvent(ContractConfirmEvent event);
}
