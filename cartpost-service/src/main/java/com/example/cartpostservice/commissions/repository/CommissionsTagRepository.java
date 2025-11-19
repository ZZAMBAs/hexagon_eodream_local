package com.example.cartpostservice.commissions.repository;

import com.example.cartpostservice.commissions.model.CommissionsTagEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionsTagRepository extends JpaRepository<CommissionsTagEntity, Long> {

    List<CommissionsTagEntity> findByCommissionCode(String commissionCode);

    void deleteByCommissionCode(String commissionCode);

    List<CommissionsTagEntity> findAllByCommissionCodeIn(List<String> commissionCodes);
}
