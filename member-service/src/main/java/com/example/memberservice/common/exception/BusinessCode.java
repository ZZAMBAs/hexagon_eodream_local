package com.example.memberservice.common.exception;

public enum BusinessCode {

    SUCCESS(0, 200, "요청을 성공적으로 처리했습니다."),

    UNAUTHORIZATION(2400, 401, "인증되지 않은 요청입니다."),

    MEMBER_NOT_FOUND(2401, 404, "요청하신 사용자를 찾을 수 없습니다."),

    DATA_SAVE_FAILED(2500, 500, "데이터 저장에 실패했습니다.");

    private final int code; // 1000번대, 2000번대

    private final int httpStatusCode;

    private final String message;

    BusinessCode(int code, int httpStatusCode, String message) {
        this.code = code;
        this.httpStatusCode = httpStatusCode;
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
