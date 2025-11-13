package com.example.profileservice.common.model.vo.exception;

import com.example.profileservice.common.model.vo.ErrorCode;
import lombok.Getter;

/**
 * 비즈니스 로직에서 발생하는 커스텀 예외 클래스
 */
@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * ErrorCode를 받아 CustomException을 생성합니다.
     * @param errorCode 발생한 비즈니스 오류 코드
     */
    public CustomException(ErrorCode errorCode) {
        // RuntimeException의 message는 ErrorCode의 메시지로 설정됩니다.
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * 특정 메시지를 추가하여 CustomException을 생성합니다.
     * (e.g., 유효하지 않은 필드명 등을 메시지에 포함할 때 사용)
     * @param errorCode 발생한 비즈니스 오류 코드
     * @param message 상세 메시지 (errorCode의 기본 메시지를 덮어씁니다.)
     */
    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
