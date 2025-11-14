package com.example.contractservice.deposit.entity;

import com.example.contractservice.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "deposit_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepositHistoryEntity extends BaseEntity {

    @Column(name = "deposit_code", nullable = false, columnDefinition = "CHAR(36)")
    private String depositCode;

    @Column(name = "code", nullable = false, columnDefinition = "CHAR(36)")
    private String code;

    @Column(name = "change_amount", nullable = false)
    private Long changeAmount;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "result_amount", nullable = false)
    private Long resultAmount;

    @Builder
    public DepositHistoryEntity(String depositCode, String code, Long changeAmount, String summary, Long resultAmount) {
        this.depositCode = depositCode;
        this.code = code;
        this.changeAmount = changeAmount;
        this.summary = summary;
        this.resultAmount = resultAmount;
    }
}
