package com.example.profileservice.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 결과 값이 없을 때 BaseResponse의 T 타입을 채우기 위한 더미 클래스
 */
@JsonSerialize
public final class Empty {
    private static final Empty INSTANCE = new Empty();

    private Empty() {}

    public static Empty getInstance() {
        return INSTANCE;
    }
}
