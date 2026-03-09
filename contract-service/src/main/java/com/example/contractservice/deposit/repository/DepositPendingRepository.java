package com.example.contractservice.deposit.repository;

import com.example.contractservice.deposit.common.DepositPendingStatus;
import com.example.contractservice.deposit.domain.DepositPending;
import com.example.contractservice.deposit.entity.DepositPendingEntity;
import com.example.contractservice.deposit.domain.exception.DepositErrorCode;
import com.example.contractservice.deposit.domain.exception.DepositException;
import com.example.contractservice.deposit.service.mapper.DepositPendingMapper;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DepositPendingRepository {
    private final DepositPendingJpaRepository depositPendingJpaRepository;

    public void save(DepositPending depositPending) {
        DepositPendingEntity entity = DepositPendingMapper.toEntity(depositPending);
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

    public List<DepositPending> findPendingBeforeWithLock(Instant cutoff, int limit) {
        return depositPendingJpaRepository.findPendingBeforeWithLock(DepositPendingStatus.PENDING.name(), cutoff, limit)
                .stream()
                .map(DepositPendingMapper::toDomain)
                .toList();
    }

    public void markCompletedByIds(List<Long> ids, Instant processedAt) {
        updateStatusByIds(ids, DepositPendingStatus.COMPLETED, processedAt);
    }

    public void markFailedByIds(List<Long> ids, Instant processedAt) {
        updateStatusByIds(ids, DepositPendingStatus.FAILED, processedAt);
    }

    private void updateStatusByIds(List<Long> ids, DepositPendingStatus status, Instant processedAt) {
        if (ids.isEmpty()) {
            return;
        }

        int updatedRows = depositPendingJpaRepository.updateStatusByIds(ids, status, processedAt);
        if (updatedRows != ids.size()) {
            throw new DepositException(DepositErrorCode.DEPOSIT_PENDING_BATCH_UPDATE_FAILED);
        }
    }

}
