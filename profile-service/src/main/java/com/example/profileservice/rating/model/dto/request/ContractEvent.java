package com.example.profileservice.rating.model.dto.request;

import java.time.Instant;

public record ContractEvent(
        String code, // 계약 코드
        String clientCode, // 클라이언트 회원 코드
        String freelancerCode, // 프리랜서 회원 코드
        Instant createdAt,
        String status
) {

}
