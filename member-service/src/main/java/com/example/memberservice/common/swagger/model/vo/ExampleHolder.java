package com.example.memberservice.common.swagger.model.vo;

import io.swagger.v3.oas.models.examples.Example;

public record ExampleHolder(
    Example holder,
    int code,
    int httpStatusCode
) {

}
