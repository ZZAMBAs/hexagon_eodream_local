package com.example.contractservice.deposit.service.dto.request;

import com.example.contractservice.deposit.domain.DepositPending;
import com.example.contractservice.deposit.domain.vo.DepositPendingDetails;

public record DepositPendingSaveRequest(
        Long amount,
        String contractCode
) {

    public DepositPending toDomain() {
        return new DepositPending(contractCode, new DepositPendingDetails(amount));
    }
}
