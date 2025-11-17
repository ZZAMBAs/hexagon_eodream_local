package com.example.memberservice.member.entity;


import com.example.memberservice.member.entity.vo.Gender;

import com.example.memberservice.common.security.model.vo.Provider;
import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.hibernate.annotations.Comment;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "members")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Members {

    //TODO(이후 common 모듈 생성시 적용)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    @Comment("외부 노출용 식별자(UUID)")
    private String code;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성 일시")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    @Comment("수정 일시")
    private Instant updatedAt;

    @Column(name = "is_deleted", nullable = false)
    @Comment("삭제 여부")
    private Boolean isDeleted = false;

    @Column(nullable = false)
    @Comment("이름")
    private String name;

    @Column(nullable = false, unique = true)
    @Comment("이메일")
    private String email;

    @Column(name = "phone_number", nullable = false)
    @Comment("핸드폰 번호")
    private String phoneNumber;

    @Column(name = "birth_date", nullable = false)
    @Comment("생년월일")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("성별")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("OAuth 제공자")
    private Provider provider;

    @Column(name = "provider_id", nullable = false)
    @Comment("OAuth 서버 제공 ID")
    private String providerId;

    @Column(name = "can_work", nullable = false)
    private Boolean canWork;

}
