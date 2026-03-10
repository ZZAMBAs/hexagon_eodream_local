package com.example.contractservice.deposit.domain.vo;

import static com.example.contractservice.deposit.domain.exception.DepositErrorCode.INVALID_PENDING_STATUS;

import com.example.contractservice.deposit.common.DepositPendingStatus;
import com.example.contractservice.deposit.domain.exception.DepositException;
import java.time.Instant;

public class DepositPendingDetails {

    private final Long amount;
    private final DepositPendingStatus status;
    private final Instant processedAt;

    public DepositPendingDetails(Long amount, DepositPendingStatus status, Instant processedAt) {
        this.amount = amount;
        this.status = status;
        this.processedAt = processedAt;
    }

    public DepositPendingDetails(Long amount) {
        this(amount, DepositPendingStatus.PENDING, null);
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

    public DepositPendingDetails complete(Instant processedAt) {
        validatePendingStatus();
        return new DepositPendingDetails(amount, DepositPendingStatus.COMPLETED, processedAt);
    }

    public DepositPendingDetails fail(Instant processedAt) {
        validatePendingStatus();
        return new DepositPendingDetails(amount, DepositPendingStatus.FAILED, processedAt);
    }

    public boolean isInvalidAmount() {
        return amount == null || amount < 0L;
    }

    public boolean hasPositiveAmount() {
        return amount != null && amount > 0L;
    }

    private void validatePendingStatus() {
        if (status != DepositPendingStatus.PENDING) {
            throw new DepositException(INVALID_PENDING_STATUS);
        }
    }
}
