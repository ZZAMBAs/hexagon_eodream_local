package com.example.contractservice.contract.service.mapper;

import com.example.contractservice.contract.domain.Contract;
import com.example.contractservice.settlement.service.dto.request.SettlementSaveRequest;

public abstract class ContractSettlementMapper {

    private ContractSettlementMapper() {}

    public static SettlementSaveRequest toSaveRequest(Contract contract) {
        return new SettlementSaveRequest(
                contract.getInfo().freelancerCode(),
                contract.getCode(),
                contract.getInfo().unitAmount(),
                contract.getInfo().startedAt(),
                contract.getInfo().endedAt(),
                contract.getInfo().paymentType());
    }

}
