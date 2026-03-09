package com.example.contractservice.deposit.repository;

import com.example.contractservice.deposit.entity.DepositEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface DepositJpaRepository extends JpaRepository<DepositEntity, Long> {

    Optional<DepositEntity> findByMemberCode(String memberCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT d
        FROM DepositEntity d
        WHERE d.memberCode = :memberCode
    """)
    Optional<DepositEntity> findByMemberCodeForUpdate(String memberCode);

    boolean existsByMemberCode(String memberCode);
}
