package com.example.contractservice.contract.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record ContractDetailResponse(
    @Schema(description = "회원 코드", example = "abdd2b21-d2a1-4d89-8271-e9941e7ef93e")
    String clientName,
    @Schema(description = "프리랜서(회원) 코드", example = "8172516b-2076-460f-805d-e60cbc0463c7")
    String freelancerName,
    @Schema(description = "계약 생성 일시", example = "2023-08-31T01:07:25.295Z")
    Instant createdAt,
    @Schema(description = "프로젝트 시작일", example = "2023-08-31T01:07:25.295Z")
    Instant startedAt,
    @Schema(description = "프로젝트 종료일", example = "2023-08-31T01:07:25.295Z")
    Instant endedAt,
    @Schema(description = "지불 방식", allowableValues = {"ONE_TIME", "MONTHLY"}, example = "MONTHLY")
    String paymentType,
    @Schema(description = "계약 상태",
        allowableValues = {"REQUESTED", "CONFIRMED", "PAID", "IN_PROGRESS", "DONE"},
        example = "PAID")
    String status,
    @Schema(description = "계약 명", example = "계약1")
    String name,
    @Schema(description = "계약 내용", example = "내용")
    String body
) {

}
