package com.example.profileservice.resume.model.entity;

import com.example.profileservice.common.model.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resumes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeEntity extends BaseEntity {

    // 회원 코드 FK
    @Column(name = "member_code", columnDefinition = "VARCHAR(36)", nullable = false, updatable = false)
    private String memberCode;

    // 제목
    @Column(length = 255)
    private String title;

    // 내용
    @Column(columnDefinition = "TEXT")
    private String body;

    // 외부 링크
    @Column(length = 512)
    private String link;

    @Builder
    public ResumeEntity(String memberCode, String title, String body, String link) {
        this.memberCode = memberCode;
        this.title = title;
        this.body = body;
        this.link = link;
    }

    public void update(String title, String body, String link) {
        this.title = title;
        this.body = body;
        this.link = link;
    }
}
