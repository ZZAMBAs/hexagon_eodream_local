package com.example.contractservice.settlement.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SettlementRepository {
    private SettlementJpaRepository settlementJpaRepository;

}
