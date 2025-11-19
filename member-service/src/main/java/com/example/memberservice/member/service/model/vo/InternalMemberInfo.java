package com.example.memberservice.member.service.model.vo;

public record InternalMemberInfo(
    String memberCode,
    String nickName,
    boolean canWork
) {

}