package com.example.cartpostservice.commissions.repository;

import com.example.cartpostservice.commissions.model.CommissionsEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionsRepository extends JpaRepository<CommissionsEntity, Long> {

    Optional<CommissionsEntity> findByCode(String code);

    List<CommissionsEntity> findByMemberCode(String memberCode);

    Optional<CommissionsEntity> findByMemberCodeAndCode(String memberCode, String commissionsCode);

    Page<CommissionsEntity> findPageByMemberCode(String memberCode, Pageable pageable);
}
