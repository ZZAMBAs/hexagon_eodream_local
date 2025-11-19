package com.example.contractservice.deposit.controller.dto.response;

import com.example.contractservice.deposit.entity.DepositEntity;
import io.swagger.v3.oas.annotations.media.Schema;

public record DepositInfoResponse (
    @Schema(description = "현재 예치금 금액", example = "0") Long amount
){
    public static DepositInfoResponse of(DepositEntity depositEntity) {
        return new DepositInfoResponse(depositEntity.getAmount());
    }
}
