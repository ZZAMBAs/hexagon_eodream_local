package com.example.memberservice.member.repository;

import com.example.memberservice.member.entity.Members;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberJpaRepository extends JpaRepository<Members, Long> {

    Optional<Members> findByCode(String code);

    List<Members> findAllByCodeInAndIsDeletedFalse(Set<String> codes);

    boolean existsByCode(String code);

}
