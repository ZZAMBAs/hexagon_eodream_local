package com.example.profileservice.common.model.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * BaseResponse의 상태를 정의하는 Enum (성공/실패 및 코드, 메시지 정의)
 */
@Getter
@RequiredArgsConstructor
public enum BaseResponseStatus {

    SUCCESS(true, 1000, HttpStatus.OK.value(), "요청에 성공했습니다.");
    
    private final boolean success;
    private final int code;
    private final int httpStatusCode;
    private final String message;
}
