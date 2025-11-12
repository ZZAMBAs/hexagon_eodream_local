package com.example.profileservice.tag.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagRequest(

        @Schema(description = "등록할 기술명", example = "Spring Boot")
        @NotBlank(message = "기술명은 필수입니다.")
        @Size(max = 100)
        String skill
) {

}
