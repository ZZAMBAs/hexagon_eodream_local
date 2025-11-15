package com.example.memberservice.common.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record ResponseDto<T>(
    @Schema(description = "front - server 간 상태코드", defaultValue = "2000")
    int code,

    @JsonProperty("httpStatus")
    int httpStatusCode,

    @Schema(description = "상태 메시지", defaultValue = "요청이 성공적으로 이루어졌습니다")
    String message,

    @Schema(description = "응답 내용", defaultValue = "XXX")
    T data
) {

}
