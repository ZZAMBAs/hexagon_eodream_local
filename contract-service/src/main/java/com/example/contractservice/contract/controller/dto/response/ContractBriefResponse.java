package com.example.contractservice.contract.controller.dto.response;

import com.example.contractservice.contract.entity.ContractEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record ContractBriefResponse(
        @Schema(description = "계약 코드", example = "8172516b-2076-460f-805d-e60cbc0463a9")
        String contractCode,
        @Schema(description = "해당 계약 생성 일자", example = "2023-08-31T01:07:25.295Z")
        Instant createdAt,
        @Schema(description = "프로젝트 시작일", example = "2023-08-31T01:07:25.295Z")
        Instant startedAt,
        @Schema(description = "프로젝트 종료일", example = "2023-09-31T01:07:25.295Z")
        Instant endedAt,
        @Schema(description = "계약 상태",
                allowableValues = {"REQUESTED", "CONFIRMED", "PAID", "IN_PROGRESS", "DONE"},
                example = "REQUESTED")
        String status,
        @Schema(description = "계약명", example = "계약명")
        String name
) {

    public static ContractBriefResponse from(ContractEntity contractEntity) {
        return new ContractBriefResponse(
                contractEntity.getCode(),
                contractEntity.getCreatedAt(),
                contractEntity.getStartedAt(),
                contractEntity.getEndedAt(),
                contractEntity.getStatus().name(),
                contractEntity.getName()
        );
    }
}
