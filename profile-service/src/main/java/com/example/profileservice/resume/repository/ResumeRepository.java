package com.example.profileservice.resume.repository;

import com.example.profileservice.resume.model.entity.ResumeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends JpaRepository<ResumeEntity, Long> {

    // memberCode로 모든 이력서 조회 (삭제되지 않은 것만, 최신순)
    @Query("SELECT r FROM ResumeEntity r " +
            "WHERE r.memberCode = :memberCode AND r.isDeleted = false " +
            "ORDER BY r.createdAt DESC")
    List<ResumeEntity> findActiveListByMemberCode(@Param("memberCode") String memberCode);

    // resumeCode로 이력서 상세 조회 (삭제되지 않은 것만)
    Optional<ResumeEntity> findByCodeAndIsDeletedFalse(String code);

    // resumeCode로 활성 상태의 이력서 존재 여부 확인
    boolean existsByCodeAndIsDeletedFalse(String code);
}
