package com.example.gatewayservice.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //401
    UNAUTHORIZATION(401, "UN_AUTHORIZATION","인증되지 않은 요청입니다."),
    NEED_RE_LOGIN(401,"NEED_RE_LOGIN", "로그인을 다시 해주세요."),
    NEED_RE_ISSUE(401, "NEED_RE_ISSUE", "AccessToken을 재발행해주세요."),
    NEED_SIGNUP(403, "NEED_SIGNUP", "회원가입이 필요합니다.");
    private final int statusCode;
    private final String code;
    private final String message;

    ErrorCode(int statusCode, String code, String message) {
        this.message = message;
        this.code = code;
        this.statusCode = statusCode;
    }

}
