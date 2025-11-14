package com.example.contractservice.settlement.service.dto.request;

import com.example.contractservice.common.PaymentType;
import java.time.Instant;

public record SettlementSaveRequest(
        String receiverCode,
        String contractCode,
        Long amount,
        Instant startedAt,
        Instant endedAt,
        PaymentType paymentType
) {

}
