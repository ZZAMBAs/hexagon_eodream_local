package com.example.contractservice.common;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResponseDto<T> (
        @Schema (description = "애플리케이션 상태 코드", example = "401")
        int code,
        @Schema (description = "애플리케이션 상태 메시지", example = "잘못된 요청 값")
        String message,
        @Schema (description = "응답 데이터")
        T data){

    public static <T> ResponseDto<T> ok(T data) {
        return new ResponseDto<>(200, "OK", data);
    }
}
