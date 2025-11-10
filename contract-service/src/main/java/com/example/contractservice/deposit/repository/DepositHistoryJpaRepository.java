package com.example.contractservice.deposit.repository;

import com.example.contractservice.deposit.entity.DepositHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositHistoryJpaRepository extends JpaRepository<DepositHistoryEntity, Long> {

}
