package com.example.profileservice.resume.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResumeCreateRequest(
        @Schema(description = "이력서 제목", example = "백엔드 개발자 이력서 1") @NotBlank(message = "제목은 필수입니다.") @Size(max = 255) String title,

        @Schema(description = "이력서 내용(자유양식)", example = "MSA 기반의 프로젝트 경험을 주로 담았습니다.") String body,

        @Schema(description = "외부 링크 (GitHub, Notion 등)", example = "https://github.com/my-profile") @Size(max = 512) String link)
{

}
