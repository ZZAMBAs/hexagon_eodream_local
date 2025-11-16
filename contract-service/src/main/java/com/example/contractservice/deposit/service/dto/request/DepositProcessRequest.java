package com.example.contractservice.deposit.service.dto.request;

import com.example.contractservice.settlement.domain.Settlement;

public record DepositProcessRequest(
        String memberCode,
        Long amount,
        String summary
) {

    public static DepositProcessRequest from(Settlement settlement) {
        return new DepositProcessRequest(
                settlement.getSettlementReference().receiverCode(),
                settlement.getSettlementStatusInfo().settledAmount(),
                "계약 정산금 입금");
    }
}
