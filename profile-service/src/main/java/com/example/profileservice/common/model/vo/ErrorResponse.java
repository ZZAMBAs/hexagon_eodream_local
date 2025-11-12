package com.example.profileservice.common.model.vo;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        // HTTP 상태 코드 (e.g., 400, 404, 409)
        int status,
        // 클라이언트가 이해할 수 있는 에러 유형 (e.g., BAD_REQUEST, NOT_FOUND)
        String error,
        // 개발자가 정의한 상세 에러 코드 (선택 사항, E001 등)
        String code,
        // 사용자 친화적인 상세 메시지
        String message
) {
    public static ErrorResponse of(HttpStatus status, String code, String message) {
        return new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                code,
                message
        );
    }
}
