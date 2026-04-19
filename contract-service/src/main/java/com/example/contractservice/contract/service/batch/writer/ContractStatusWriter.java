package com.example.contractservice.contract.service.batch.writer;

import com.example.contractservice.contract.domain.Contract;
import com.example.contractservice.contract.repository.batch.ContractBatchRepository;
import com.example.contractservice.contract.repository.batch.dto.ContractBatchUpdateTarget;
import com.example.contractservice.contract.service.mapper.ContractMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.ApplicationEventPublisher;

@StepScope
@RequiredArgsConstructor
public abstract class ContractStatusWriter implements ItemWriter<Contract> {

    protected final ApplicationEventPublisher applicationEventPublisher;
    protected final ContractBatchRepository contractBatchRepository;

    @Override
    public void write(Chunk<? extends Contract> chunk) {
        List<? extends Contract> contracts = chunk.getItems();
        List<ContractBatchUpdateTarget> targets = new ArrayList<>();
        Instant batchTime = Instant.now();

        contracts.forEach(contract -> {
            Instant previousUpdatedAt = contract.getUpdatedAt();

            changeStatus(contract);

            targets.add(ContractBatchUpdateTarget.from(contract, previousUpdatedAt, batchTime));
        });

        contractBatchRepository.updateAllStatusInBatch(targets);

        contracts.forEach(this::publishEvent);
    }

    protected void publishEvent(Contract contract) {
        applicationEventPublisher.publishEvent(ContractMapper.toContractEvent(contract));
    }

    protected abstract void changeStatus(Contract contract);
}
