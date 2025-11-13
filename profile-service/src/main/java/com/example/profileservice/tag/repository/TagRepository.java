package com.example.profileservice.tag.repository;

import com.example.profileservice.tag.model.entity.TagEntity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    // 기술명(skill)으로 태그 중복 여부 확인
    boolean existsBySkillIgnoreCase(String skill);

    // 코드로 태그 조회
    Optional<TagEntity> findByCode(String code);

    // 코드 목록으로 태그 조회
    List<TagEntity> findAllByCodeIn(Collection<String> codes);
}
