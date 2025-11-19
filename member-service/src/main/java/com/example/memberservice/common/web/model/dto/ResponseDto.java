package com.example.memberservice.common.web.model.dto;

import com.example.memberservice.common.exception.ErrorCode;
import com.example.memberservice.common.web.model.vo.Empty;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

public record ResponseDto<T>(
    @Schema(description = "front - server 간 상태코드", defaultValue = "0")
    int code,

    @JsonProperty("httpStatus")
    @Schema(description = "Http Status 코드", defaultValue = "200")
    int httpStatusCode,

    @Schema(description = "상태 메시지", defaultValue = SUCCESS_MESSAGE)
    String message,

    @Schema(description = "응답 내용", defaultValue = "XXX")
    T data
) {

    private static final int SUCCESS_CODE = 0;

    private static final String SUCCESS_MESSAGE = "요청이 성공하였습니다.";

    // 요청에 성공한 경우 (결과 값 없음)
    public static ResponseDto<Empty> success() {
        return new ResponseDto<>(
            SUCCESS_CODE,
            HttpStatus.OK.value(),
            SUCCESS_MESSAGE,
            Empty.getInstance()
        );
    }

    // 요청에 성공한 경우 (Http Status 값 있음)
    public static ResponseDto<Empty> success(HttpStatus httpStatusCode) {
        return new ResponseDto<>(
            SUCCESS_CODE,
            httpStatusCode.value(),
            SUCCESS_MESSAGE,
            Empty.getInstance()
        );
    }

    // 요청에 성공한 경우 (결과 값 있음)
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(
            SUCCESS_CODE,
            HttpStatus.OK.value(),
            SUCCESS_MESSAGE,
            data
        );
    }

    public static ResponseDto<Empty> fail(ErrorCode errorCode, String message){
        return new ResponseDto<>(
            errorCode.getCode(),
            errorCode.getHttpStatusCode(),
            message,
            Empty.getInstance()
        );
    }

    public static ResponseDto<Empty> fail(ErrorCode errorCode) {
        return new ResponseDto<>(
            errorCode.getCode(),
            errorCode.getHttpStatusCode(),
            errorCode.getMessage(),
            Empty.getInstance()
        );
    }

    public static ResponseDto<Empty> fail(){
        return ResponseDto.fail(ErrorCode.INTERNAL_SERVER_ERROR);
    }

}
