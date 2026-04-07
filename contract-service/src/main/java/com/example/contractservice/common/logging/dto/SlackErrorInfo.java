package com.example.contractservice.common.logging.dto;

public record SlackErrorInfo(
        String httpMethod,
        String httpPath,
        String loggingClass,
        String exceptionSummary,
        String rootCause
) {

}
