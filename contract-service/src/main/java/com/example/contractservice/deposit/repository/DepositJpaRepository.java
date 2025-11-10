package com.example.contractservice.deposit.repository;

import com.example.contractservice.deposit.entity.DepositEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositJpaRepository extends JpaRepository<DepositEntity, Long> {

}
