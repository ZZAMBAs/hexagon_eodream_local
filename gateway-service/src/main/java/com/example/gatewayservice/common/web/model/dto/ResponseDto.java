package com.example.gatewayservice.common.web.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseDto<T>(

    int code,

    @JsonProperty("httpStatus")
    int httpStatusCode,

    String message,

    T data
) {

}
