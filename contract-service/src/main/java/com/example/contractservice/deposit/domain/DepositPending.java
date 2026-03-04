package com.example.contractservice.deposit.domain;

import com.example.contractservice.deposit.domain.vo.DepositPendingDetails;
import java.time.Instant;

public class DepositPending {
    private Long id;
    private String contractCode;

    private DepositPendingDetails depositPendingDetails;

    private Instant createdAt;

    public DepositPending(String contractCode, DepositPendingDetails depositPendingDetails, Instant createdAt) {
        this.contractCode = contractCode;
        this.depositPendingDetails = depositPendingDetails;
        this.createdAt = createdAt;
    }

    public void markCompleted() {
        this.depositPendingDetails = depositPendingDetails.completed();
    }

    // getter
    public Long getId() {
        return id;
    }

    public String getContractCode() {
        return contractCode;
    }

    public DepositPendingDetails getDepositPendingDetails() {
        return depositPendingDetails;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
