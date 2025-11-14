package com.example.profileservice.experience.model.entity;

import com.example.profileservice.common.model.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "experiences")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExperienceEntity extends BaseEntity {

    // 이력서 코드 FK
    @Column(name = "resume_code", columnDefinition = "VARCHAR(36)", nullable = false, updatable = false)
    private String resumeCode;

    // 회사명/활동명
    @Column(length = 255, nullable = false)
    private String title;

    // 기관명/팀명
    @Column(length = 255, nullable = false)
    private String organization;

    // 경력/경험 내용
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    // 시작일
    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    // 종료일
    @Column(name = "ended_at")
    private Instant endedAt;

    @Builder
    public ExperienceEntity(String resumeCode, String title, String organization, String description, Instant startedAt, Instant endedAt, String job, String position) {
        this.resumeCode = resumeCode;
        this.title = title;
        this.organization = organization;
        this.description = description;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    // 정적 팩토리 메서드로 생성 로직 캡슐화
    public static ExperienceEntity create(String resumeCode, String title, String organization, String description, Instant startedAt, Instant endedAt) {
        return ExperienceEntity.builder()
                .resumeCode(resumeCode)
                .title(title)
                .organization(organization)
                .description(description)
                .startedAt(startedAt)
                .endedAt(endedAt)
                .build();
    }

    public void update(String title, String organization, String description, Instant startedAt, Instant endedAt) {
        this.title = title;
        this.organization = organization;
        this.description = description;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    // 경력/경험 항목이 해당 이력서에 속하는지 확인
    public boolean belongsTo(String resumeCode) {
        return this.resumeCode.equals(resumeCode);
    }
}
