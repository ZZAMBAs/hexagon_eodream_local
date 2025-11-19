package com.example.profileservice.experience.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public record ExperienceRequest(
        @Schema(description = "회사명/활동명", example = "프리랜서 매칭 서비스 개발 프로젝트")
        @NotBlank(message = "활동명은 필수입니다.")
        @Size(max = 255)
        String title,

        @Schema(description = "기관명/팀명", example = "헥사곤")
        @NotBlank(message = "기관명/팀명은 필수입니다.")
        @Size(max = 255)
        String organization,

        @Schema(description = "경력/경험 내용 (자유 양식)", example = "MSA 환경에서 API 게이트웨이 구축 및 비즈니스 로직 개발")
        @NotBlank(message = "경력/경험 내용은 필수입니다.")
        String description,

        @Schema(description = "시작일", example = "2023-01-01T00:00:00Z")
        @NotNull(message = "시작일은 필수입니다.")
        Instant startedAt,

        @Schema(description = "종료일 (진행 중이면 Null)", example = "2023-12-31T23:59:59Z")
        Instant endedAt
) {

}
