package com.example.contractservice.contract.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record ContractBriefResponse(
    @Schema(description = "계약 요청 회원 이름", example = "계약 요청자 이름")
    String requestorName,
    @Schema(description = "계약 성립자 회원 이름", example = "계약 상대방 이름")
    String contractorName,
    @Schema(description = "프로젝트 시작일", example = "2023-08-31T01:07:25.295Z")
    Instant startedAt,
    @Schema(description = "프로젝트 종료일", example = "10000")
    Instant endedAt,
    @Schema(description = "계약 상태",
        allowableValues = {"REQUESTED", "CONFIRMED", "PAID", "IN_PROGRESS", "DONE"},
        example = "REQUESTED")
    String status,
    @Schema(description = "계약명", example = "계약명")
    String name
) {

}
