package com.example.contractservice.settlement.domain.vo;

import java.time.Instant;

public record SettlementTimeline(
    Instant createdAt,
    Instant settledAt,
    Instant progressingAt
) {

}
