package com.example.contractservice.contract.repository;

import com.example.contractservice.contract.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractJpaRepository extends JpaRepository<ContractEntity, Long> {

}
