package com.example.paymentservice.payment.model;

import com.example.paymentservice.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "orders")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends BaseEntity {

    @Column(nullable = false, updatable = false)
    private String memberCode;

    @Column(nullable = false, unique = true, updatable = false)
    private String orderPgId;

    @Override
    public void prePersistHook() {
        if (this.orderPgId == null) {
            this.orderPgId = UUID.randomUUID().toString();
        }
    }

    @Builder
    public OrderEntity(String memberCode) {
        this.memberCode = memberCode;
    }
}

