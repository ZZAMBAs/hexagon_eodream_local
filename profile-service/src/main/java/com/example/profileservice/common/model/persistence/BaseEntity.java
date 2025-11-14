package com.example.profileservice.common.model.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    // 고유 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 외부 노출용 식별자
    @Column(columnDefinition = "VARCHAR(36)", updatable = false, unique = true, nullable = false)
    private String code;

    // 생성 일시
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // 수정 일시
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // 삭제 여부
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    // 엔티티 저장 전 UUID 생성
    @PrePersist
    public void prePersist() {
        if (this.code == null) {
            this.code = UUID.randomUUID().toString();
        }
    }

    // 삭제 처리
    public void delete() {
        this.isDeleted = true;
    }
}
