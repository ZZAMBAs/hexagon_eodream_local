package com.example.contractservice.settlement.domain.vo;

import com.example.contractservice.settlement.common.SettlementStatus;
import java.math.BigDecimal;

public record SettlementStatusInfo(
    Long originalAmount,
    Long settledAmount,
    SettlementStatus status,
    BigDecimal settlementRate
) {

}
