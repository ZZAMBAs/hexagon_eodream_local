package com.example.cartpostservice.cart.model;

import com.example.cartpostservice.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "carts")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartsEntity extends BaseEntity {

    @Column(name = "member_code", nullable = false, updatable = false)
    private String memberCode;

}
