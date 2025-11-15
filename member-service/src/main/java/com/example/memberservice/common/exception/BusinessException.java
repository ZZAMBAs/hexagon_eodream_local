package com.example.memberservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BusinessException extends RuntimeException {

    private final BusinessCode businessCode;

    public BusinessException(BusinessCode businessCode) {
        super(businessCode.getMessage());
        this.businessCode = businessCode;
    }
}
