package com.example.cartpostservice.commissions.repository;

import com.example.cartpostservice.commissions.model.CommissionsTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionsTagRepository extends JpaRepository<CommissionsTagEntity, Long> {

}
