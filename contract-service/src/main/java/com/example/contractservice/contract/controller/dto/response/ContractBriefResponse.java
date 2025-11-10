package com.example.contractservice.contract.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record ContractBriefResponse(
    @Schema(description = "회원 이름", example = "회원이름")
    String clientName,
    @Schema(description = "프리랜서(회원) 이름", example = "프리랜서이름")
    String freelancerName,
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
