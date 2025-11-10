package com.example.contractservice.contract.entity;

import com.example.contractservice.contract.common.ContractStatus;
import com.example.contractservice.contract.common.PaymentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "contracts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // BaseEntity

    @Column(name = "freelancer_code", nullable = false, columnDefinition = "CHAR(36)")
    private String freelancerCode;

    @Column(name = "client_code", nullable = false, columnDefinition = "CHAR(36)")
    private String clientCode;

    @Column(name = "code", nullable = false, columnDefinition = "CHAR(36)")
    private String code;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt; // BaseEntity

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt; // BaseEntity

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false; // BaseEntity

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

}
