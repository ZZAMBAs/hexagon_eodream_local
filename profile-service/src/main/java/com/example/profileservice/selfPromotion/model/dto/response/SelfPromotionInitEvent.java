package com.example.profileservice.selfPromotion.model.dto.response;

import java.time.Instant;
import java.util.List;

public record SelfPromotionInitEvent(
        Instant createdAt,
        List<SelfPromotionEsEventData> selfPromotions
) {
    public static SelfPromotionInitEvent create(List<SelfPromotionEsEventData> dataList) {
        return new SelfPromotionInitEvent(Instant.now(), dataList);
    }
}
