package com.example.contractservice.contract.service.dto.request;

import com.example.contractservice.contract.common.Order;
import java.time.Instant;

public record ContractReadCursorRequest(
        String memberCode, // 로그인 사용자 코드
        Instant cursor, // 커서 (생성 날짜, created_at)
        String cursorCode, // 커서 (계약 코드, contracts의 code)
        Order order) // 정렬 방향
{

}
