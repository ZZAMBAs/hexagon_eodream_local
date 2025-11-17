package com.example.paymentservice.payment.model;

import com.example.paymentservice.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "payments")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity extends BaseEntity {

    @Column(nullable = false, unique = true, updatable = false)
    private String orderPgId;     // OrderEntity와 논리적 연결

    @Column(updatable = false)
    private String paymentKey;  // PG사 고유 키

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Builder
    public PaymentEntity(String orderPgId, String paymentKey, Long amount, PaymentStatus paymentStatus) {
        this.orderPgId = orderPgId;
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }
}


