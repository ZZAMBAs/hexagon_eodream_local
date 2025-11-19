package com.example.contractservice.deposit.service.dto.request;

import java.time.Instant;

public record DepositHistoryCursorRequest(
        String memberCode,
        Instant cursorDate,
        String cursorCode) {

}
