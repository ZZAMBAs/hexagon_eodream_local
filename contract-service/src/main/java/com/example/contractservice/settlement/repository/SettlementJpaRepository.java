package com.example.contractservice.settlement.repository;

import com.example.contractservice.settlement.entity.SettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementJpaRepository extends JpaRepository<SettlementEntity, Long> {

}
