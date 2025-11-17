package com.example.cartpostservice.cart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "cart_items")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "contract_code", nullable = false, updatable = false)
    private String contractCode;

    @Column(name = "cart_code", nullable = false, updatable = false)
    private String cartCode;

    @Column(name = "status", nullable = false, updatable = false)
    private String status;
}
