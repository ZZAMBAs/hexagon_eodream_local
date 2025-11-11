package com.example.memberservice.member.controller.dto.vo;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberTag(

    @Schema(description = "기술 태그 코드", defaultValue = "UUID.toString()")
    String tagCode,

    @Schema(description = "기술태그 이름", defaultValue = "Java")
    String tagName

) {

}
