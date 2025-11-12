package com.example.contractservice.contract.domain.exception;

public enum ContractErrorCode {
    NO_FREELANCERS(100, "프리랜서가 없어 계약을 생성할 수 없습니다."),
    INVALID_MEMBER(101, "존재하지 않는 회원으로 계약을 생성할 수 없습니다."),
    DELETED_MEMBER(102, "탈퇴한 회원은 계약을 생성할 수 없습니다."),

    NO_CONTRACT(110, "해당 계약이 존재하지 않습니다."), // TODO: 계약 코드를 넣을 수 있도록 개선
    NOT_CONTRACTOR(111, "현재 로그인한 회원이 계약 성립자가 아닙니다."),
    NOT_REQUESTED_STATUS(112, "요청 상태인 계약만 성립할 수 있습니다.");

    private final int statusCode;
    private final String message;

    ContractErrorCode(int statusCode, String message) {
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
