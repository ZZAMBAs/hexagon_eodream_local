package com.example.profileservice.resume.repository;

import com.example.profileservice.resume.model.entity.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends JpaRepository<ResumeEntity, String> {

}
