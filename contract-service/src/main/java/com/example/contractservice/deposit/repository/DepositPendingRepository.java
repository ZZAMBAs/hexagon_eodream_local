package com.example.contractservice.deposit.repository;

import com.example.contractservice.deposit.common.DepositPendingStatus;
import com.example.contractservice.deposit.domain.DepositPending;
import com.example.contractservice.deposit.entity.DepositPendingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DepositPendingRepository {
    private final DepositPendingJpaRepository depositPendingJpaRepository;

    public void save(DepositPending depositPending) {
        DepositPendingEntity entity = DepositPendingEntity.toEntity(depositPending);
        depositPendingJpaRepository.save(entity);
    }

    public boolean cancelPendingByContractCode(String contractCode) {
        int updatedRows = depositPendingJpaRepository.updateStatusByContractCode(
                contractCode,
                DepositPendingStatus.PENDING,
                DepositPendingStatus.CANCELLED
        );

        return updatedRows > 0;
    }

}
