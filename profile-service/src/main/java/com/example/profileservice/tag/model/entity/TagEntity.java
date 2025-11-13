package com.example.profileservice.tag.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagEntity {

    // 고유 아이디 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기술명
    @Column(length = 100, nullable = false, unique = true)
    private String skill;

    // 기술 코드
    @Column(length = 36, nullable = false, unique = true, updatable = false)
    private String code;

    @Builder
    public TagEntity(String skill) {
        this.code = UUID.randomUUID().toString();
        this.skill = skill;
    }
}
