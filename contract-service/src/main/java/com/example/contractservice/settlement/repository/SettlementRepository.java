package com.example.contractservice.settlement.repository;

import static com.example.contractservice.settlement.domain.exception.SettlementErrorCode.SETTLEMENT_ALREADY_PROCESSED;
import static com.example.contractservice.settlement.domain.exception.SettlementErrorCode.SETTLEMENT_NOT_EXISTS;
import static com.example.contractservice.settlement.service.mapper.SettlementMapper.applyToEntity;
import static com.example.contractservice.settlement.service.mapper.SettlementMapper.toDomain;
import static com.example.contractservice.settlement.service.mapper.SettlementMapper.toEntity;

import com.example.contractservice.settlement.common.SettlementStatus;
import com.example.contractservice.settlement.domain.Settlement;
import com.example.contractservice.settlement.domain.exception.SettlementException;
import com.example.contractservice.settlement.entity.SettlementEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SettlementRepository {
    private final SettlementJpaRepository settlementJpaRepository;

    public Settlement save(Settlement settlement) {
        Optional<SettlementEntity> optionalSettlement = settlementJpaRepository.findByCode(settlement.getCode());

        if (optionalSettlement.isPresent()) {
            SettlementEntity settlementEntity = optionalSettlement.get();

            applyToEntity(settlement, settlementEntity);
            return toDomain(settlementJpaRepository.save(settlementEntity));
        }

        SettlementEntity settlementEntity = toEntity(settlement);

        return toDomain(settlementJpaRepository.save(settlementEntity));
    }

    public void deleteCancelableSettlementsByContractCode(String contractCode) {
        int deletedCount = settlementJpaRepository.deleteCancelableByContractCode(contractCode, SettlementStatus.DONE.name());

        if (deletedCount > 0) { // 정상적으로 삭제
            return;
        }

        // 이미 정산 처리 되었는지 최종 확인 (모종의 이유로 정산 데이터가 없었을 수도 있기 때문)
        if (settlementJpaRepository.existsByContractCodeAndStatus(contractCode, SettlementStatus.DONE)) {
            throw new SettlementException(SETTLEMENT_ALREADY_PROCESSED);
        }

        // 정상적이지 않은 흐름(정산 데이터 없음)
        throw new SettlementException(SETTLEMENT_NOT_EXISTS);
    }
}
