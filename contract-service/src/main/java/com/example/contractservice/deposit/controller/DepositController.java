package com.example.contractservice.deposit.controller;

import com.example.contractservice.deposit.common.GetDepositApi;
import com.example.contractservice.deposit.common.GetDepositHistoriesApi;
import com.example.contractservice.deposit.controller.dto.response.DepositHistoryInfoResponse;
import com.example.contractservice.deposit.controller.dto.response.DepositInfoResponse;
import com.example.contractservice.deposit.service.DepositService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deposits")
public class DepositController {
    private final DepositService depositService;

    @GetDepositApi
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public DepositInfoResponse getMyDeposit(@RequestHeader(name = "X-CODE") String xCode) {

        return new DepositInfoResponse(1000L);
    }

    @GetDepositHistoriesApi
    @GetMapping("/histories")
    @ResponseStatus(HttpStatus.OK)
    public List<DepositHistoryInfoResponse> getMyDepositHistories(
        @RequestHeader(value = "X-CODE") String xCode,
        @RequestParam(value = "cursor-date", required = false) Instant cursorDate,
        @RequestParam(value = "cursor-code", required = false) String cursorCode) {

        return List.of(new DepositHistoryInfoResponse(Instant.now(), 1000L, 2000L, "요약 예시"));
    }
}
