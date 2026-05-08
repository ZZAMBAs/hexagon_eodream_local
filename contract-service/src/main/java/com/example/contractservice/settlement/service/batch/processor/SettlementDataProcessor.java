package com.example.contractservice.settlement.service.batch.processor;

import com.example.contractservice.settlement.domain.Settlement;
import com.example.contractservice.settlement.entity.SettlementEntity;
import com.example.contractservice.settlement.service.mapper.SettlementMapper;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class SettlementDataProcessor implements ItemProcessor<SettlementEntity, Settlement> {

    @Value("${batch.settlement.settlement-rate}")
    private BigDecimal settlementRate;

    @Override
    public Settlement process(SettlementEntity settlementEntity) {
        Settlement settlement = SettlementMapper.toDomain(settlementEntity);

        return validateSettlement(settlement);
    }

    private Settlement validateSettlement(Settlement settlement) {
        if (!settlement.isValid()) {
            Settlement failed = settlement.fail();

            if (log.isWarnEnabled())
                log.warn("정산할 수 없는 데이터입니다. FAILED 처리됩니다. id: {}, contract code: {}", settlement.getId(), settlement.getSettlementReference().contractCode());

            // TODO: Micrometer로 기록
            return failed;
        }
        // TODO: Micrometer로 기록
        return settlement.settle(settlementRate);
    }
}
