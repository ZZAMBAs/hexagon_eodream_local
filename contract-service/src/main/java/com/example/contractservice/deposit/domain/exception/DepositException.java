package com.example.contractservice.deposit.domain.exception;

public class DepositException extends RuntimeException {
    public final DepositErrorCode errorCode;

    public DepositException(DepositErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
