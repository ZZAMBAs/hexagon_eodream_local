package com.example.contractservice.settlement.service.dto.response;

import com.example.contractservice.settlement.domain.Settlement;

public record SettlementSaveResponse(
        String code
) {

    public static SettlementSaveResponse from(Settlement settlement) {
        return new SettlementSaveResponse(settlement.getCode());
    }
}
