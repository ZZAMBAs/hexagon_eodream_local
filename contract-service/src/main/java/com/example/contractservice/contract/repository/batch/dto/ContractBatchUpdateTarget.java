package com.example.contractservice.contract.repository.batch.dto;

import com.example.contractservice.contract.common.ContractStatus;
import com.example.contractservice.contract.domain.Contract;
import java.time.Instant;

public record ContractBatchUpdateTarget(
        String code,
        ContractStatus nextStatus,
        Instant previousUpdatedAt,
        Instant newUpdatedAt
) {

    public static ContractBatchUpdateTarget from(Contract contract, Instant previousUpdatedAt, Instant newUpdatedAt) {
        return new ContractBatchUpdateTarget(
                contract.getCode(),
                contract.getInfo().status(),
                previousUpdatedAt,
                newUpdatedAt
        );
    }
}
