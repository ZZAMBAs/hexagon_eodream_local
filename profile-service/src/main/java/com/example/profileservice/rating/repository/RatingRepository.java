package com.example.profileservice.rating.repository;

import com.example.profileservice.rating.model.entity.RatingEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {

    // receiverCode로 RatingEntity를 조회합니다. (primary key는 id지만, 조회는 code로 함)
    Optional<RatingEntity> findByReceiverCode(String receiverCode);

    // 만족 카운트를 1 증가시키는 쿼리
    @Modifying
    @Transactional
    @Query("UPDATE RatingEntity r SET r.satisfiedCount = r.satisfiedCount + 1 WHERE r.receiverCode = :receiverCode")
    int incrementSatisfiedCount(@Param("receiverCode") String receiverCode);

    // 불만족 카운트를 1 증가시키는 쿼리
    @Modifying
    @Transactional
    @Query("UPDATE RatingEntity r SET r.unsatisfiedCount = r.unsatisfiedCount + 1 WHERE r.receiverCode = :receiverCode")
    int incrementUnsatisfiedCount(@Param("receiverCode") String receiverCode);

    // receiverCode 존재 여부 확인
    boolean existsByReceiverCode(String receiverCode);
}
