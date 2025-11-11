package com.example.contractservice.contract.domain.exception;

public class ContractCreateException extends RuntimeException {
    private final CreateErrorCode errorCode;

    public ContractCreateException(CreateErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ContractCreateException(Throwable cause, CreateErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public CreateErrorCode getErrorCode() {
        return errorCode;
    }
}
