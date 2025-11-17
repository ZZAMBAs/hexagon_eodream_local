package com.example.cartpostservice.commissions.controller.dto.request;

import com.example.cartpostservice.common.model.vo.PaymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;


public record CommissionCreateRequest(

        @Schema(description = "의뢰 제목", example = "디자인 작업 요청")
        @NotBlank
        String title,

        @Schema(description = "의뢰 내용 상세", example = "상세 설명을 여기에 입력하세요.")
        @NotNull
        String content,

        @Schema(description = "결제 방식", example = "PER_JOB")
        @NotNull
        PaymentType paymentType,

        @Schema(description = "단위 금액", example = "50000")
        @NotBlank
        String unitAmount,

        @Schema(description = "시작 날짜 (yyyy-MM-dd)", example = "2025-01-01")
        @NotNull
        LocalDate startedAt,

        @Schema(description = "종료 날짜 (yyyy-MM-dd)", example = "2025-01-31")
        @NotNull
        LocalDate endedAt,

        @Schema(description = "태그 코드 리스트", example = "[\"tag-uuid-1\", \"tag-uuid-2\"]")
        @NotNull
        List<String> tagCode

) {

}
