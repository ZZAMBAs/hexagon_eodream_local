package com.example.contractservice.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // BaseEntity

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private Instant createdAt; // BaseEntity

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt; // BaseEntity

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false; // BaseEntity

    protected void touchUpdatedAt() {
        this.updatedAt = Instant.now();
    }

}
