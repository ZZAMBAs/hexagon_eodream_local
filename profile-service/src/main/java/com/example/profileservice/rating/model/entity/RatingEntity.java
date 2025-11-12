package com.example.profileservice.rating.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "ratings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RatingEntity {

    // 고유 아이디 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 평가를 받은 회원의 코드 FK
    @Column(name = "receiver_code", columnDefinition = "VARCHAR(36)", nullable = false, updatable = false, unique = true)
    private String receiverCode;

    // 받은 '만족' 개수
    @Column(name = "satisfied_count", nullable = false)
    private int satisfiedCount = 0;

    // 받은 '불만족' 개수
    @Column(name = "unsatisfied_count", nullable = false)
    private int unsatisfiedCount = 0;

    @Builder
    public RatingEntity(String receiverCode) {
        this.receiverCode = receiverCode;
    }
}
