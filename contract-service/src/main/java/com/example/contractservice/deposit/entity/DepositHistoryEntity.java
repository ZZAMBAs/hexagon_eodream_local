package com.example.contractservice.deposit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "deposit_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepositHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // BaseEntity

    @Column(name = "deposit_code", nullable = false, columnDefinition = "CHAR(36)")
    private String depositCode;

    @Column(name = "code", nullable = false, columnDefinition = "CHAR(36)")
    private String code;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt; // BaseEntity

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt; // BaseEntity

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false; // BaseEntity

    @Column(name = "change_amount", nullable = false)
    private Long changeAmount;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "result_amount", nullable = false)
    private Long resultAmount;

}
