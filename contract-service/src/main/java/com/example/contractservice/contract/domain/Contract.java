package com.example.contractservice.contract.domain;

import com.example.contractservice.contract.domain.vo.ContractContent;
import com.example.contractservice.contract.domain.vo.ContractInfo;
import java.time.Instant;
import java.util.UUID;

public class Contract {

    private String code;

    private ContractInfo info;
    private ContractContent content;

    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isDeleted;

    public Contract(String code, ContractInfo info, ContractContent content, Instant createdAt,
        Instant updatedAt, Boolean isDeleted) {
        this.code = (code == null) ? generateCode() : code;
        this.info = info;
        this.content = content;
        this.createdAt = (createdAt == null) ? Instant.now() : createdAt;
        this.updatedAt = (updatedAt == null) ? this.createdAt : updatedAt;
        this.isDeleted = isDeleted;
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }
}
