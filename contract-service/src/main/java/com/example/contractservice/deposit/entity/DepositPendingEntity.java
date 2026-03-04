package com.example.contractservice.deposit.entity;

import com.example.contractservice.common.entity.BaseEntity;
import com.example.contractservice.deposit.common.DepositPendingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
        name = "deposit_pendings",
        indexes = {
                @Index(name = "idx_deposit_pending_status_created_at", columnList = "status, processed_at")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_deposit_pending_code", columnNames = "code")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepositPendingEntity extends BaseEntity {

    @Column(name = "member_code", nullable = false, columnDefinition = "CHAR(36)")
    private String memberCode;

    @Column(name = "contract_code", nullable = false, columnDefinition = "CHAR(36)")
    private String contractCode;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DepositPendingStatus status;

    @Column(name = "processed_at")
    private Instant processedAt;

    private DepositPendingEntity(String memberCode, String contractCode, Long amount) {
        this.memberCode = memberCode;
        this.contractCode = contractCode;
        this.amount = amount;
        this.status = DepositPendingStatus.PENDING;
    }

    public static DepositPendingEntity create(String memberCode, String contractCode, Long amount) {
        return new DepositPendingEntity(memberCode, contractCode, amount);
    }

    public void markCompleted() {
        this.status = DepositPendingStatus.COMPLETED;
        this.processedAt = Instant.now();
    }

}
