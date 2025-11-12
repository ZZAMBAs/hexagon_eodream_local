package com.example.profileservice.tag.repository;

import com.example.profileservice.tag.model.entity.MemberTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTagRepository extends JpaRepository<MemberTagEntity, String> {

}
