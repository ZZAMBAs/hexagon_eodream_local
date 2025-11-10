package com.example.contractservice.deposit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record DepositHistoryInfoResponse(
    @Schema(description = "내역 변경 일시", example = "2023-08-31T01:07:25.295Z") Instant createdAt,
    @Schema(description = "변경된 금액", example = "2000") Long changeAmount,
    @Schema(description = "처리 후 잔액", example = "1452000") Long resultAmount,
    @Schema(description = "변경 내역에 대한 짧은 요약", example = "디자이너 계약금 정산") String summary
) {

}
