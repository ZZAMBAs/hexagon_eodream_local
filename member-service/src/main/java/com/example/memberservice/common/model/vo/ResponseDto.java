package com.example.memberservice.common.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResponseDto<T>(
    @Schema(description = "상태코드", defaultValue = "200")
    int code,

    @Schema(description = "상태 메시지", defaultValue = "이상없음")
    String message,

    @Schema(description = "응답 내용", defaultValue = "XXX")
    T data
) {}
