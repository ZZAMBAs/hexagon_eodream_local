package com.example.memberservice.member.controller.dto.vo;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberRating(

    @Schema(description = "사용자 만족 평가 받은 수", defaultValue = "10")
    int satisfiedCount,

    @Schema(description = "사용자 불만족 평가 받은 수", defaultValue = "2")
    int unsatisfiedCount
) {

}
