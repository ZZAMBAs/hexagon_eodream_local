package com.example.profileservice.tag.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TagResponse(

        @Schema(description = "태그 고유 식별 코드", example = "tag-uuid-code-001")
        String tagCode,

        @Schema(description = "기술명", example = "Spring Boot")
        String skill
) {

}
