package com.example.contractservice.contract.domain.vo;

import com.example.contractservice.contract.common.ContractStatus;
import com.example.contractservice.contract.common.PaymentType;
import java.time.Instant;

public record ContractInfo(
    String freelancerCode,
    String clientCode,
    Instant startedAt,
    Instant endedAt,
    PaymentType paymentType,
    Long unitAmount,
    ContractStatus status
) {

}
