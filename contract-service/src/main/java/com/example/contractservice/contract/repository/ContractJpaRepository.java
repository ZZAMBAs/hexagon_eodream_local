package com.example.contractservice.contract.repository;

import com.example.contractservice.contract.entity.ContractEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractJpaRepository extends JpaRepository<ContractEntity, Long> {

    Optional<ContractEntity> findByCode(String code);
}
