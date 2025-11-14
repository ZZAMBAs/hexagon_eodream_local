package com.example.contractservice.contract.event.dto;

import java.time.Instant;

public record ContractEvent(
        String code,
        Instant createdAt,
        String status
) {

}
