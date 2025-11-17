package com.example.cartpostservice.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class FieldErrorDetail {

    @Schema(description = "에러가 발생한 필드명", example = "email")
    private String field;

    @Schema(description = "에러가 발생한 이유 (검증 메시지)", example = "이메일 형식이 올바르지 않습니다.")
    private String reason;

    @Schema(description = "제출된 잘못된 값", example = "testUser@")
    private String value;

    private FieldErrorDetail(FieldError error) {
        this.field = error.getField();
        this.reason = error.getDefaultMessage();
        this.value = error.getRejectedValue() != null ? error.getRejectedValue().toString() : "null";
    }

    public static List<FieldErrorDetail> of(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
                .map(FieldErrorDetail::new)
                .collect(Collectors.toList());
    }

}
