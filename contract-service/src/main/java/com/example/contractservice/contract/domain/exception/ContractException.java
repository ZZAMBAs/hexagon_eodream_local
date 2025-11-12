package com.example.contractservice.contract.domain.exception;

public class ContractException extends RuntimeException {
    private final ContractErrorCode errorCode;

    public ContractException(ContractErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ContractException(Throwable cause, ContractErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ContractErrorCode getErrorCode() {
        return errorCode;
    }
}
