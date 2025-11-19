package com.example.profileservice.selfPromotion.model.dto.response;

import com.example.profileservice.common.model.vo.PaymentType;
import java.time.Instant;

public record SelfPromotionEsEventData(
        String code,
        String title,
        String content,
        String memberCode,
        String memberNickname,
        PaymentType paymentType,
        Long unitAmount,
        Instant updatedAt
) {
    public static SelfPromotionEsEventData fromResponse(SelfPromotionResponse response) {
        return new SelfPromotionEsEventData(
                response.promotionCode(),
                response.title(),
                response.content(),
                response.memberCode(),
                response.memberNickname(),
                response.paymentType(),
                response.unitAmount(),
                response.updatedAt()
        );
    }
}
