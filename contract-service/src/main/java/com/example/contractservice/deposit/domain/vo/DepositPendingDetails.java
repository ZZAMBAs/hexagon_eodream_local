package com.example.contractservice.deposit.domain.vo;

import static com.example.contractservice.deposit.domain.exception.DepositErrorCode.INVALID_STATUS_FOR_COMPLETED;

import com.example.contractservice.deposit.common.DepositPendingStatus;
import com.example.contractservice.deposit.domain.exception.DepositException;
import java.time.Instant;

public class DepositPendingDetails {

    private Long amount;
    private DepositPendingStatus status;
    private Instant processedAt;

    public DepositPendingDetails(Long amount, Instant processedAt) {
        this.amount = amount;
        this.processedAt = processedAt;
        this.status = DepositPendingStatus.PENDING;
    }

    private DepositPendingDetails(Long amount, DepositPendingStatus status, Instant processedAt) {
        this.amount = amount;
        this.status = status;
        this.processedAt = processedAt;
    }

    public DepositPendingDetails completed() {
        if (this.status != DepositPendingStatus.PENDING) {
            throw new DepositException(INVALID_STATUS_FOR_COMPLETED);
        }

        return new DepositPendingDetails(this.amount, DepositPendingStatus.COMPLETED, Instant.now());
    }

    public Long getAmount() {
        return amount;
    }

    public DepositPendingStatus getStatus() {
        return status;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }
}
