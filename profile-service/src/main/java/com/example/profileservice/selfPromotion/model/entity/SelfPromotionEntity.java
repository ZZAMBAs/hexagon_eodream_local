package com.example.profileservice.selfPromotion.model.entity;

import com.example.profileservice.common.model.persistence.BaseEntity;
import com.example.profileservice.common.model.vo.PaymentType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "self_promotions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SelfPromotionEntity extends BaseEntity {

    // 프로필 작성 회원의 코드 FK
    @Column(name = "member_code", columnDefinition = "VARCHAR(36)", nullable = false, updatable = false)
    private String memberCode;

    // 제목
    @Column(length = 255, nullable = false)
    private String title;

    // 내용(어필)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // 지급 방식: enum payment_type
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false, length = 20)
    private PaymentType paymentType;

    // 단위 금액
    @Column(name = "unit_amount", nullable = false)
    private Long unitAmount;

    // 이력서 code FK
    @Column(name = "resume_code", columnDefinition = "VARCHAR(36)")
    private String resumeCode;

    @Builder
    public SelfPromotionEntity(String memberCode, String title, String content, PaymentType paymentType, Long unitAmount, String resumeCode) {
        this.memberCode = memberCode;
        this.title = title;
        this.content = content;
        this.paymentType = paymentType;
        this.unitAmount = unitAmount;
        this.resumeCode = resumeCode;
    }

    public void update(String title, String content, PaymentType paymentType, Long unitAmount, String resumeCode) {
        this.title = title;
        this.content = content;
        this.paymentType = paymentType;
        this.unitAmount = unitAmount;
        this.resumeCode = resumeCode;
    }
}
