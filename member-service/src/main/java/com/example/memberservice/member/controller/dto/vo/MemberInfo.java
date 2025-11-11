package com.example.memberservice.member.controller.dto.vo;


import io.swagger.v3.oas.annotations.media.Schema;

public record MemberInfo(

    @Schema(description = "사용자 닉네임", defaultValue = "이어드림 팬 1")
    String name,

    @Schema(description = "사용자 이메일", defaultValue = "devthkim0317@gmail.com")
    String email,

    @Schema(description = "사용자 전화번호", defaultValue = "010-0000-0000")
    String phoneNumber,

    @Schema(description = "사용자 생일정보", defaultValue = "YYYY-MM-DD")
    String birthDay,

    @Schema(description = "사용자 성별", defaultValue = "MAN OR FEMAIL")
    String gender

) {

}
