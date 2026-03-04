package com.example.contractservice.deposit.entity;

import com.example.contractservice.deposit.common.DepositPendingStatus;
import com.example.contractservice.deposit.domain.DepositPending;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@Table(
        name = "deposit_pendings",
        indexes = {
                @Index(name = "idx_status_created_at", columnList = "status, created_at")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepositPendingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "datetime(6)")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false, columnDefinition = "datetime(6)")
    private Instant updatedAt;

    @Column(name = "contract_code", nullable = false, columnDefinition = "CHAR(36)")
    private String contractCode;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DepositPendingStatus status;

    @Column(name = "processed_at", columnDefinition = "datetime(6)")
    private Instant processedAt;

    private DepositPendingEntity(String contractCode, Long amount) {
        this.contractCode = contractCode;
        this.amount = amount;
        this.status = DepositPendingStatus.PENDING;
    }

    public static DepositPendingEntity toEntity(DepositPending depositPending) {
        return new DepositPendingEntity(
                depositPending.getContractCode(),
                depositPending.getDepositPendingDetails().getAmount()
        );
    }

}
