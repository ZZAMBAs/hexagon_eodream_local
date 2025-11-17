package com.example.cartpostservice.cart.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "장바구니 아이템 목록 조회 응답 DTO")
public record CartItemsGetResponse(

        @Schema(description = "아이템 코드", example = "UUID")
        String code,

        @Schema(description = "계약 코드", example = "UUID")
        String contractCode,

        @Schema(description = "시작 일시", example = "2025-11-17T10:00:00Z")
        Instant startedAt,

        @Schema(description = "종료 일시", example = "2025-12-17T10:00:00Z")
        Instant endedAt,

        @Schema(description = "지불 방식", example = "MONTHLY, PER_JOB")
        String paymentType,

        @Schema(description = "결제 금액", example = "100000")
        Long amount

) {}

