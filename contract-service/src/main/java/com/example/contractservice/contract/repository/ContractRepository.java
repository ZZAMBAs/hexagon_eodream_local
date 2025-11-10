package com.example.contractservice.contract.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ContractRepository {
    private final ContractJpaRepository contractJpaRepository;

}
