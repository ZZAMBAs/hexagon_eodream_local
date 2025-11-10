package com.example.contractservice.deposit.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DepositRepository {
    private final DepositJpaRepository depositJpaRepository;
    private final DepositHistoryJpaRepository depositHistoryJpaRepository;

}
