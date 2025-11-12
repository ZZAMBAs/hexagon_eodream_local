package com.example.profileservice.experience.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record ExperienceResponse(
        @Schema(description = "경험 고유 식별 코드", example = "sa546a6-asd7f-sd57fs-sd5f7ds567ds5d")
        String experienceCode,

        @Schema(description = "회사명/활동명", example = "프리랜서 매칭 서비스 개발 프로젝트")
        String title,

        @Schema(description = "기관명/팀명", example = "헥사곤")
        String organization,

        @Schema(description = "경력/경험 내용", example = "MSA 환경에서 API 게이트웨이 구축 및 비즈니스 로직 개발")
        String description,

        @Schema(description = "시작일", example = "2023-01-01T00:00:00Z")
        Instant startedAt,

        @Schema(description = "종료일 (진행 중이면 Null)", example = "2023-12-31T23:59:59Z")
        Instant endedAt
) {

}
