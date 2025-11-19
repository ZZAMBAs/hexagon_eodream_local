package com.example.cartpostservice.commissions.controller.dto.response;

import com.example.cartpostservice.common.model.vo.PaymentType;
import java.time.LocalDate;
import java.util.List;

public record CommissionElementReadResponse(

        String title,

        String content,

        PaymentType paymentType,

        String unitAmount,

        LocalDate startedAt,

        LocalDate endedAt,

        boolean isOpen,

        String writerName,

        List<String> tagCode
) {

}
