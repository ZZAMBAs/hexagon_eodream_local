package com.example.contractservice.contract.event.dto;

import java.time.Instant;

public record ContractConfirmEvent(
        String code,
        Instant createdAt
) {

}
