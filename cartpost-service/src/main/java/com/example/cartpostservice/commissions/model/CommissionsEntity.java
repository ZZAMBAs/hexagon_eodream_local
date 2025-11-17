package com.example.cartpostservice.commissions.model;

import com.example.cartpostservice.common.model.vo.PaymentType;
import com.example.cartpostservice.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Table(name = "commissions")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommissionsEntity extends BaseEntity {

    private String memberCode;

    private String title;

    private String content;

    private PaymentType paymentType;

    private String unitAmount;

    private LocalDate startedAt;

    private LocalDate endedAt;

    private boolean isOpen;

    private String writerName;

}
