package com.example.contractservice.deposit.domain;

import com.example.contractservice.deposit.domain.vo.DepositChange;
import java.time.Instant;
import java.util.UUID;

public class DepositHistory {
    private String code;
    private String depositCode;

    private DepositChange depositChange;

    private String summary;

    private Instant createdAt;

    public DepositHistory(String code, String depositCode, DepositChange depositChange,
        String summary, Instant createdAt) {
        this.code = (code == null) ? generateCode() : code;
        this.depositCode = depositCode;
        this.depositChange = depositChange;
        this.summary = summary;
        this.createdAt = (createdAt == null) ? Instant.now() : createdAt;
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }
}
