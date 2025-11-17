package com.example.memberservice.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    //400
    VALIDATION_FAILED(2000, HttpStatus.BAD_REQUEST, "유효성 검증 실패"),

    //401
    FAIL_LOGIN(2200, HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다."),
    UNAUTHORIZATION(2201, HttpStatus.UNAUTHORIZED, "인증되지 않은 요청입니다."),

    //404
    MEMBER_NOT_FOUND(2400, HttpStatus.NOT_FOUND, "요청하신 사용자를 찾을 수 없습니다."),
    INTERNAL_ILLEGAL_MEMBER_CODE(2401, HttpStatus.NOT_FOUND, "존재하지 않는 멤버 코드가 포함되어 있습니다."),
    NO_HANDLER(2402, HttpStatus.NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다."),

    //500
    INTERNAL_SERVER_ERROR(2500, HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다."),
    DATA_SAVE_FAILED(2501, HttpStatus.INTERNAL_SERVER_ERROR, "데이터 저장에 실패했습니다.");

    private final int code; // 2000번대

    private final int httpStatusCode;

    private final String message;

    ErrorCode(int code, HttpStatus httpStatusCode, String message) {
        this.code = code;
        this.httpStatusCode = httpStatusCode.value();
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getMessage() {
        return message;
    }

}
