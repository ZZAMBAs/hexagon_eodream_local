package com.example.memberservice.member.service.model.vo;

public record MemberInfo(
    String memberCode,
    String nickName,
    boolean canWork
) {

}