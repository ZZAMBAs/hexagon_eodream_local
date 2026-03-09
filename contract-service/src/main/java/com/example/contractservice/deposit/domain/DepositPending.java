package com.example.contractservice.deposit.domain;

import static com.example.contractservice.deposit.domain.exception.DepositErrorCode.INVALID_PENDING_STATUS;

import com.example.contractservice.deposit.common.DepositPendingStatus;
import com.example.contractservice.deposit.domain.exception.DepositException;
import com.example.contractservice.deposit.domain.vo.DepositPendingDetails;
import java.time.Instant;

public class DepositPending {
    private final Long id;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String contractCode;
    private final DepositPendingDetails depositPendingDetails;

    public DepositPending(Long id, Instant createdAt, Instant updatedAt, String contractCode,
            DepositPendingDetails depositPendingDetails) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.contractCode = contractCode;
        this.depositPendingDetails = depositPendingDetails;
    }

    public DepositPending(Long id, String contractCode, DepositPendingDetails depositPendingDetails) {
        this(id, null, null, contractCode, depositPendingDetails);
    }

    public DepositPending(String contractCode, DepositPendingDetails depositPendingDetails) {
        this(null, contractCode, depositPendingDetails);
    }

    public boolean isInvalidAmount() {
        return depositPendingDetails.isInvalidAmount();
    }

    public boolean hasPositiveAmount() {
        return depositPendingDetails.hasPositiveAmount();
    }

    public DepositPending complete(Instant processedAt) {
        return new DepositPending(id, createdAt, processedAt, contractCode, depositPendingDetails.complete(processedAt));
    }

    public DepositPending fail(Instant processedAt) {
        return new DepositPending(id, createdAt, processedAt, contractCode, depositPendingDetails.fail(processedAt));
    }

    // getter
    public Long getId() {
        return id;
    }

    public String getContractCode() {
        return contractCode;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public DepositPendingDetails getDepositPendingDetails() {
        return depositPendingDetails;
    }

    public Long getAmount() {
        return depositPendingDetails.getAmount();
    }

    public DepositPendingStatus getStatus() {
        return depositPendingDetails.getStatus();
    }

    public Instant getProcessedAt() {
        return depositPendingDetails.getProcessedAt();
    }

}
