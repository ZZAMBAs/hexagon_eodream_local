package com.example.contractservice.settlement.service.batch.writer;

import com.example.contractservice.settlement.entity.SettlementEntity;
import com.example.contractservice.settlement.repository.SettlementRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class SettlementDataWriter implements ItemWriter<SettlementEntity> {
    public final SettlementRepository settlementRepository;

    @Override
    public void write(Chunk<? extends SettlementEntity> chunk) {
        settlementRepository.saveAll((List<SettlementEntity>) chunk.getItems());
    }
}
