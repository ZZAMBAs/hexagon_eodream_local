package com.example.contractservice.deposit.service.mapper;

import com.example.contractservice.deposit.domain.Deposit;
import com.example.contractservice.deposit.domain.DepositHistory;
import com.example.contractservice.deposit.domain.vo.DepositChange;
import com.example.contractservice.deposit.entity.DepositHistoryEntity;

public abstract class DepositHistoryMapper {

    private DepositHistoryMapper() {}

    public static DepositHistoryEntity toEntity(DepositHistory depositHistory) {
        DepositChange depositChange = depositHistory.getDepositChange();

        return DepositHistoryEntity.builder()
                .code(depositHistory.getCode())
                .depositCode(depositHistory.getDepositCode())
                .changeAmount(depositChange.changeAmount())
                .resultAmount(depositChange.resultAmount())
                .summary(depositHistory.getSummary())
                .build();
    }

    public static DepositHistory toDomain(Deposit deposit, Long changeAmount, String summary) {
        DepositChange depositChange = new DepositChange(changeAmount, deposit.getAmount());

        return new DepositHistory(deposit.getCode(), depositChange, summary);
    }
}
