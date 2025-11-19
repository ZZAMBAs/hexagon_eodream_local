package com.example.profileservice.selfPromotion.model.dto.request;

import com.example.profileservice.common.model.vo.OperationType;
import com.example.profileservice.selfPromotion.model.dto.response.SelfPromotionEsEventData;
import java.time.Instant;

public record SelfPromotionEvent(
        OperationType operationType,
        Instant createdAt,
        SelfPromotionEsEventData data,
        String deletedCode
) {
    public static SelfPromotionEvent create(SelfPromotionEsEventData data) {
        return new SelfPromotionEvent(OperationType.CREATE, Instant.now(), data, null);
    }

    public static SelfPromotionEvent update(SelfPromotionEsEventData data) {
        return new SelfPromotionEvent(OperationType.UPDATE, Instant.now(), data, null);
    }

    // 삭제 시에는 코드(String)만 인수로 받기
    public static SelfPromotionEvent delete(String promotionCode) {
        return new SelfPromotionEvent(OperationType.DELETE, Instant.now(), null, promotionCode);
    }
}
