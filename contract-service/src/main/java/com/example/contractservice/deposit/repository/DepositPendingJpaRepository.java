package com.example.contractservice.deposit.repository;

import com.example.contractservice.deposit.common.DepositPendingStatus;
import com.example.contractservice.deposit.entity.DepositPendingEntity;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
        WHERE dp.status = :status
          AND dp.created_at < :cutoff
        ORDER BY dp.status, dp.created_at
        LIMIT :limit
        FOR UPDATE SKIP LOCKED
    """, nativeQuery = true)
    List<DepositPendingEntity> findPendingBeforeWithLock(
            @Param("status") String status,
            @Param("cutoff") Instant cutoff,
            @Param("limit") int limit
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE DepositPendingEntity dp
        SET dp.status = :status,
            dp.processedAt = :processedAt,
            dp.updatedAt = :processedAt
        WHERE dp.id IN :ids
    """)
    int updateStatusByIds(
            @Param("ids") List<Long> ids,
            @Param("status") DepositPendingStatus status,
            @Param("processedAt") Instant processedAt
    );

}
