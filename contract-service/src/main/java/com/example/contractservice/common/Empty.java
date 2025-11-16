package com.example.contractservice.common;

public class Empty {
    private static final Empty INSTANCE = new Empty();

    private Empty() {}

    public static Empty getInstance() {
        return INSTANCE;
    }
}
