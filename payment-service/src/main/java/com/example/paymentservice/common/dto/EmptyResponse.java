package com.example.paymentservice.common.dto;

public final class EmptyResponse {

    private static final EmptyResponse INSTANCE = new EmptyResponse();

    private EmptyResponse() {
    }

    public static EmptyResponse getInstance() {
        return INSTANCE;
    }
}
