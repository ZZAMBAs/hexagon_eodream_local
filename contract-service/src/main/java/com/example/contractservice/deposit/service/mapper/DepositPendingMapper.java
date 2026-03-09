package com.example.contractservice.deposit.service.mapper;

import com.example.contractservice.deposit.domain.DepositPending;
import com.example.contractservice.deposit.domain.vo.DepositPendingDetails;
import com.example.contractservice.deposit.entity.DepositPendingEntity;

public abstract class DepositPendingMapper {

    private DepositPendingMapper() {}

    public static DepositPendingEntity toEntity(DepositPending depositPending) {
        return DepositPendingEntity.create(
                depositPending.getContractCode(),
                depositPending.getDepositPendingDetails().getAmount()
        );
    }

    public static DepositPending toDomain(DepositPendingEntity depositPendingEntity) {
        DepositPendingDetails pendingDetails = new DepositPendingDetails(
                depositPendingEntity.getAmount(),
                depositPendingEntity.getStatus(),
                depositPendingEntity.getProcessedAt()
        );

        return new DepositPending(
                depositPendingEntity.getId(),
                depositPendingEntity.getCreatedAt(),
                depositPendingEntity.getUpdatedAt(),
                depositPendingEntity.getContractCode(),
                pendingDetails
        );
    }
}
