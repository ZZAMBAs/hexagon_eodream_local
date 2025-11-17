package com.example.cartpostservice.common.model.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    MONTHLY("월급"), PER_JOB("건당");

    private final String description;
}
