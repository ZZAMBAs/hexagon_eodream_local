package com.example.gatewayservice.common.web.model.dto;

public record ResponseDto<T>(

    int code,

    String message,

    T data
) {

}
