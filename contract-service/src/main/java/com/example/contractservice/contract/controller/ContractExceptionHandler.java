package com.example.contractservice.contract.controller;

import com.example.contractservice.common.ResponseDto;
import com.example.contractservice.contract.domain.exception.ContractException;
import com.example.contractservice.contract.domain.exception.ContractErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ContractExceptionHandler {

    @ExceptionHandler(ContractException.class)
    public ResponseEntity<ResponseDto<Void>> handleContractCreateException(ContractException e) {
        ContractErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>(errorCode.getStatusCode(), errorCode.getMessage(), null));
    }

}
