package com.example.contractservice.settlement.repository;

import com.example.contractservice.settlement.common.SettlementStatus;
import com.example.contractservice.settlement.entity.SettlementEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SettlementJpaRepository extends JpaRepository<SettlementEntity, Long> {

    Optional<SettlementEntity> findByCode(String code);

    @Modifying
    @Query(value = """
        DELETE s
        FROM settlements s
        LEFT JOIN settlements blocked
               ON blocked.contract_code = s.contract_code
              AND blocked.status = :blockedStatus
        WHERE s.contract_code = :contractCode
          AND blocked.contract_code IS NULL
    """, nativeQuery = true)
    int deleteCancelableByContractCode(String contractCode, String blockedStatus);

    boolean existsByContractCodeAndStatus(String contractCode, SettlementStatus status);
}
