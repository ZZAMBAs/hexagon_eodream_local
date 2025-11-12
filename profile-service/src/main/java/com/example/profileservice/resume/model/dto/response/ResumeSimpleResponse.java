package com.example.profileservice.resume.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record ResumeSimpleResponse(
        @Schema(description = "이력서 외부 식별자 코드", example = "sa546a6-asd7f-sd57fs-sd5f7ds567ds5d")
        String resumeCode,

        @Schema(description = "이력서 제목", example = "백엔드 개발자 이력서 1")
        String title,

        @Schema(description = "생성 일시")
        Instant createdAt
) {

}
