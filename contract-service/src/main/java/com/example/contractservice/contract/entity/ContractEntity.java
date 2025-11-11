package com.example.contractservice.contract.entity;

import com.example.contractservice.common.entity.BaseEntity;
import com.example.contractservice.contract.common.ContractStatus;
import com.example.contractservice.contract.common.PaymentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "contracts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContractEntity extends BaseEntity {

    @Column(name = "requestor_code", nullable = false, columnDefinition = "CHAR(36)")
    private String requestorCode;

    @Column(name = "contractor_code", nullable = false, columnDefinition = "CHAR(36)")
    private String contractorCode;

    @Column(name = "code", nullable = false, columnDefinition = "CHAR(36)")
    private String code;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "ended_at", nullable = false)
    private Instant endedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "unit_amount", nullable = false)
    private Long unitAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContractStatus status;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Builder
    public ContractEntity(String requestorCode, String contractorCode, String code, Instant startedAt, Instant endedAt,
            PaymentType paymentType, Long unitAmount, ContractStatus status, String name, String body) {
        this.requestorCode = requestorCode;
        this.contractorCode = contractorCode;
        this.code = code;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.paymentType = paymentType;
        this.unitAmount = unitAmount;
        this.status = status;
        this.name = name;
        this.body = body;
    }
}
