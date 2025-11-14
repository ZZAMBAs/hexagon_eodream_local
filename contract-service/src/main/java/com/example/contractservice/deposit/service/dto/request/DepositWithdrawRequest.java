package com.example.contractservice.deposit.service.dto.request;

public record DepositWithdrawRequest(
        String memberCode,
        Long amount,
        String summary
) {

}
