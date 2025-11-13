package com.example.memberservice.socialmember.repository;

import com.example.memberservice.socialmember.entity.SocialMembers;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialMemberJpaRepository extends JpaRepository<SocialMembers, Long> {

    Optional<SocialMembers> findSocialMembersByCode(String code);

    Optional<SocialMembers> findSocialMembersByProviderId(String providerId);

}
