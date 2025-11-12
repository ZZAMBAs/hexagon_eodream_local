package com.example.contractservice.contract.repository;

import static com.example.contractservice.contract.domain.exception.ContractErrorCode.*;

import com.example.contractservice.contract.domain.exception.ContractException;
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

    public ContractEntity findByCode(String code) {
        return contractJpaRepository.findByCode(code)
                .orElseThrow(() -> new ContractException(NO_CONTRACT));
    }
}
