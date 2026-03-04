package com.example.contractservice.deposit.repository;

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

}
