package com.example.cartpostservice.common.dto;

import com.example.cartpostservice.common.exception.CustomStatusCode;
import com.example.cartpostservice.common.exception.FieldErrorDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.validation.BindingResult;

public record ResponseDto<T>(
        @Schema(description = "성공 및 실패에 대한 구체적인 개별 커스텀 코드", defaultValue = "0")
        int code,

        @JsonProperty("httpStatus")
        @Schema(description = "상태코드", defaultValue = "200")
        int httpStatusCode,

        @Schema(description = "상태 메시지", defaultValue = "이상없음")
        String message,

        @Schema(description = "응답 내용")
        T data
) {

    public static <T> ResponseDto<T> success(CustomStatusCode customStatusCode, T responseData) {
        return new ResponseDto<>(
                customStatusCode.getCode(),
                customStatusCode.getStatus().value(),
                customStatusCode.getMessage(),
                responseData
        );
    }

    //data가 없는 에러 응답 (BusinessException)
    public static ResponseDto<EmptyResponse> createEmptyErrorResponse(CustomStatusCode customStatusCode) {
        return new ResponseDto<>(
                customStatusCode.getCode(),
                customStatusCode.getStatus().value(),
                customStatusCode.getMessage(),
                EmptyResponse.getInstance()
        );
    }

    //@Valid 유효성 검사 에러 응답 (MethodArgumentNotValidException)
    public static ResponseDto<List<FieldErrorDetail>> createValidationErrorResponse(CustomStatusCode customStatusCode, BindingResult bindingResult) {
        return new ResponseDto<>(
                customStatusCode.getCode(),
                customStatusCode.getStatus().value(),
                customStatusCode.getMessage(),
                FieldErrorDetail.of(bindingResult) // data에 필드 에러 목록 추가
        );
    }
}

