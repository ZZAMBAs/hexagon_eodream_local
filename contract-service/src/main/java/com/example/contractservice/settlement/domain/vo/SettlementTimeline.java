package com.example.contractservice.settlement.domain.vo;

import java.time.Instant;

public record SettlementTimeline(
    Instant createdAt,
    Instant settledAt,
    Instant progressingAt,
    Instant failedAt
) {

    public SettlementTimeline(Instant progressingAt) {
        this(null, null, progressingAt);
    }

    public SettlementTimeline(Instant createdAt, Instant settledAt, Instant progressingAt) {
        this((createdAt == null) ? Instant.now() : createdAt, settledAt, progressingAt, null);
    }

    public SettlementTimeline updateSettledAt(Instant curSettledAt) {
        return new SettlementTimeline(createdAt, curSettledAt, progressingAt);
    }

    public SettlementTimeline updateFailedAt(Instant curFailedAt) {
        return new SettlementTimeline(createdAt, settledAt, progressingAt, curFailedAt);
    }
}
