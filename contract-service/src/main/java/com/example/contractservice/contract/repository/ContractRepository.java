package com.example.contractservice.contract.repository;

import com.example.contractservice.contract.entity.ContractEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ContractRepository {
    private final ContractJpaRepository contractJpaRepository;

    public ContractEntity saveContract(ContractEntity contractEntity) {
        return contractJpaRepository.save(contractEntity);
    }
}
