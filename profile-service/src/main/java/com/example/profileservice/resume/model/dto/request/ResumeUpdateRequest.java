package com.example.profileservice.resume.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResumeUpdateRequest(
        @Schema(description = "수정할 이력서 제목", example = "백엔드 개발자 이력서 1 (최종)")
        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 255)
        String title,

        @Schema(description = "수정할 이력서 내용(자유양식)", example = "최근 프로젝트 결과를 반영했습니다.")
        String body,

        @Schema(description = "수정할 외부 링크 (GitHub, Notion 등)", example = "https://notion.so/my-resume")
        @Size(max = 512)
        String link
) {

}
