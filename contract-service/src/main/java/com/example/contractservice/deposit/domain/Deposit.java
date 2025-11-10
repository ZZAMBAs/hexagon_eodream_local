package com.example.contractservice.deposit.domain;

import java.util.UUID;

public class Deposit {

    private String code;

    private String memberCode;

    private Long amount;

    public Deposit(String code, String memberCode, Long amount) {
        this.code = (code == null) ? generateCode() : code;
        this.memberCode = memberCode;
        this.amount = amount;
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }
}
