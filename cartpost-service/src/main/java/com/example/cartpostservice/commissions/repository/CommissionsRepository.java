package com.example.cartpostservice.commissions.repository;

import com.example.cartpostservice.commissions.model.CommissionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionsRepository extends JpaRepository<CommissionsEntity, Long> {

}
