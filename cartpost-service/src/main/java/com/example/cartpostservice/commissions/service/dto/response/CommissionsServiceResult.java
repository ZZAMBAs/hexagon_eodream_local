package com.example.cartpostservice.commissions.service.dto.response;

import com.example.cartpostservice.common.model.vo.PaymentType;
import java.time.LocalDate;

public record CommissionsServiceResult(
        String code,

        String memberCode,

        String title,

        String content,

        PaymentType paymentType,

        String unitAmount,

        LocalDate startedAt,

        LocalDate endedAt,

        boolean isOpen,

        String writerName
) {

}
