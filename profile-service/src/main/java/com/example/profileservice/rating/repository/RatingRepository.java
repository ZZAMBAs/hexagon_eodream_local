package com.example.profileservice.rating.repository;

import com.example.profileservice.rating.model.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, String> {

}
