package com.example.contractservice.contract.service.event.dto;

import java.time.Instant;

public record ContractEvent(
        String code,
        Instant createdAt,
        String status
) {

}
