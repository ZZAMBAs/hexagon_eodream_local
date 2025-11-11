package com.example.contractservice.contract.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ContractCreateResponse(
    @Schema(description = "생성된 계약 코드", example = "4cd54740-91dc-4bcd-856c-1b776fc227b6")
    String code
) {

    public static ContractCreateResponse of(String code) {
        return new ContractCreateResponse(code);
    }
}
