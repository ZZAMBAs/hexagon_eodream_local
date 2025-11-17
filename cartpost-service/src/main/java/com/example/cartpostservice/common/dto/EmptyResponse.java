package com.example.cartpostservice.common.dto;

public final class EmptyResponse {

    private static final EmptyResponse INSTANCE = new EmptyResponse();

    private EmptyResponse() {
    }

    public static EmptyResponse getInstance() {
        return INSTANCE;
    }

    public String getEmpty() {
        return ""; // 빈 필드 하나라도 있으면 직렬화 가능
    }
}