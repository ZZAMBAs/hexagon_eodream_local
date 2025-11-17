package com.example.paymentservice.common.exception;

import com.example.paymentservice.common.dto.enums.CustomStatusCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final CustomStatusCode customStatusCode;

    public BusinessException(CustomStatusCode customStatusCode) {
        super(customStatusCode.getMessage()); // RuntimeException의 message 필드에 메시지를 설정합니다.
        this.customStatusCode = customStatusCode;
    }
}
