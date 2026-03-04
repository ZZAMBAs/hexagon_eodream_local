package com.example.contractservice.deposit.repository;

import com.example.contractservice.deposit.entity.DepositPendingEntity;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepositPendingJpaRepository extends JpaRepository<DepositPendingEntity, Long> {

    @Query(value = """
        SELECT *
        FROM deposit_pendings dp
        WHERE dp.status = :status AND dp.processedAt <= :endTime
        ORDER BY dp.status, dp.processedAt
        LIMIT :limit
    """, nativeQuery = true)
    List<DepositPendingEntity> findAllByStatusOrderByProcessedAt(String status, Instant endTime, int limit);
}
