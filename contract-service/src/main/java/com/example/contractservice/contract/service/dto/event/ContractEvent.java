package com.example.contractservice.contract.service.dto.event;

import java.time.Instant;

public record ContractEvent(
        String code,
        Instant createdAt,
        String status
) {

}
