package com.example.contractservice.deposit.service.dto.response;

public record DepositWithdrawResponse(
        String code,
        String memberCode,
        Long amount
) {

}
