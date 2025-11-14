package com.example.contractservice.settlement.domain.vo;

import static com.example.contractservice.settlement.common.SettlementStatus.*;

import com.example.contractservice.settlement.common.SettlementStatus;
import java.math.BigDecimal;

public record SettlementStatusInfo(
    Long originalAmount,
    Long settledAmount,
    SettlementStatus status,
    BigDecimal settlementRate
) {

    public SettlementStatusInfo(Long originalAmount) {
        this(originalAmount, null, BEFORE, null);
    }
}
