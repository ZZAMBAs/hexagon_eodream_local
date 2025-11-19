package com.example.cartpostservice.commissions.controller.dto.response;

public record MemberResponse(
        String memberCode,
        String nickName,
        boolean canWork
) {

}
