package com.example.contractservice.settlement.domain.vo;

import java.time.Instant;

public record SettlementTimeline(
    Instant createdAt,
    Instant settledAt,
    Instant progressingAt
) {

    public SettlementTimeline(Instant progressingAt) {
        this(null, null, progressingAt);
    }

    public SettlementTimeline(Instant createdAt, Instant settledAt, Instant progressingAt) {
        this.createdAt = (createdAt == null) ? Instant.now() : createdAt;
        this.settledAt = settledAt;
        this.progressingAt = progressingAt;
    }
}
