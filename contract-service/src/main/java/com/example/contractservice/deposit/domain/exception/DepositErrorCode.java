package com.example.contractservice.deposit.domain.exception;

public enum DepositErrorCode {
    NO_DEPOSIT_ENTITY(200, "해당하는 예치금 엔티티가 없습니다."),
    NOT_ENOUGH_AMOUNT(201, "예치금 잔액이 부족합니다.");

    private final int statusCode;
    private final String message;

    DepositErrorCode(int statusCode, String message) {
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
