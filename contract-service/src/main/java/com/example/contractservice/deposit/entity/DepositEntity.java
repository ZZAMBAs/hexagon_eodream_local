package com.example.contractservice.deposit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "deposits")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepositEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_code", nullable = false, columnDefinition = "CHAR(36)")
    private String memberCode;

    @Column(name = "code", nullable = false, columnDefinition = "CHAR(36)")
    private String code;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Builder
    public DepositEntity(String memberCode, String code, Long amount) {
        this.memberCode = memberCode;
        this.code = code;
        this.amount = amount;
    }

    public void updateInfo(Long amount) {
        this.amount = amount;
    }
}
