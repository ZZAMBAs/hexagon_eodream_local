package com.example.memberservice.member.service.model.dto.input;

public record MemberExistByNameInput(
    String memberCode,
    String name
) {

}