package com.example.profileservice.selfPromotion.repository;

import com.example.profileservice.selfPromotion.model.entity.SelfPromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfPromotionRepository extends JpaRepository<SelfPromotionEntity, String> {

}
