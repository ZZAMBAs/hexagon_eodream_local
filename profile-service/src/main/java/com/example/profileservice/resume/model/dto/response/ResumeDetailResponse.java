package com.example.profileservice.resume.model.dto.response;

import com.example.profileservice.experience.model.dto.response.ExperienceResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

public record ResumeDetailResponse(
        @Schema(description = "이력서 고유 식별 코드", example = "sa546a6-asd7f-sd57fs-sd5f7ds567ds5d")
        String resumeCode,

        @Schema(description = "이력서 제목", example = "백엔드 개발자 이력서 1")
        String title,

        @Schema(description = "이력서 내용(자유양식)", example = "MSA 기반의 프로젝트 경험을 주로 담았습니다.")
        String body,

        @Schema(description = "외부 링크 (GitHub, Notion 등)", example = "https://github.com/my-profile")
        String link,

        @Schema(description = "생성 일시", example = "2025-11-10T03:00:00Z")
        Instant createdAt,

        @Schema(description = "수정 일시", example = "2025-11-10T03:00:00Z")
        Instant updatedAt,

        @Schema(description = "포함된 경력/경험 목록")
        List<ExperienceResponse> experiences
) {

}
