package com.example.memberservice.member.repository;

import com.example.memberservice.member.entity.Members;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberJpaRepository extends JpaRepository<Members, Long> {

    Optional<Members> findByCode(String code);

}
