package com.example.contractservice.deposit.repository;

import com.example.contractservice.deposit.common.DepositPendingStatus;
import com.example.contractservice.deposit.entity.DepositPendingEntity;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DepositPendingJpaRepository extends JpaRepository<DepositPendingEntity, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE DepositPendingEntity dp
        SET dp.status = :nextStatus,
            dp.updatedAt = CURRENT_TIMESTAMP
        WHERE dp.contractCode = :contractCode
          AND dp.status = :currentStatus
    """)
    int updateStatusByContractCode(String contractCode, DepositPendingStatus currentStatus, DepositPendingStatus nextStatus);

    @Query(value = """
        SELECT *
        FROM deposit_pendings dp
        WHERE dp.status = :status AND dp.processedAt <= :endTime
        ORDER BY dp.status, dp.processedAt
        LIMIT :limit
    """, nativeQuery = true)
    List<DepositPendingEntity> findAllByStatusOrderByProcessedAt(String status, Instant endTime, int limit);
}
