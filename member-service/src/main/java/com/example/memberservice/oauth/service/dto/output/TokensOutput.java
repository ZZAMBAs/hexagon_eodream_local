package com.example.memberservice.oauth.service.dto.output;

public record TokensOutput(
    String accessToken,
    String refreshToken
) {

}
