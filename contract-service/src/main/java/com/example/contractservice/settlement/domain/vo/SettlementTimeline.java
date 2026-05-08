package com.example.contractservice.settlement.domain.vo;

import java.time.Instant;
import java.time.LocalDate;

public record SettlementTimeline(
    Instant createdAt,
    Instant settledAt,
    LocalDate progressingAt,
    Instant failedAt
) {

    public SettlementTimeline(LocalDate progressingAt) {
        this(null, null, progressingAt);
    }

    public SettlementTimeline(Instant createdAt, Instant settledAt, LocalDate progressingAt) {
        this((createdAt == null) ? Instant.now() : createdAt, settledAt, progressingAt, null);
    }

    public SettlementTimeline settle() {
        return new SettlementTimeline(createdAt, Instant.now(), progressingAt);
    }

    public SettlementTimeline fail() {
        return new SettlementTimeline(createdAt, settledAt, progressingAt, Instant.now());
    }
}
