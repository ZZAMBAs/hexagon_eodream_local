package com.example.contractservice.contract.domain.exception;

public enum CreateErrorCode {
    NO_FREELANCERS(100, "프리랜서가 없어 계약을 생성할 수 없습니다."),
    INVALID_MEMBER(101, "존재하지 않는 회원으로 계약을 생성할 수 없습니다."),
    DELETED_MEMBER(102, "탈퇴한 회원은 계약을 생성할 수 없습니다.");

    private final int statusCode;
    private final String message;

    CreateErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
