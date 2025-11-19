package com.example.contractservice.contract.service.batch.writer;

import com.example.contractservice.contract.entity.ContractEntity;
import com.example.contractservice.contract.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.ApplicationEventPublisher;

@StepScope
@RequiredArgsConstructor
public abstract class ContractStatusWriter implements ItemWriter<ContractEntity> {

    protected final ApplicationEventPublisher applicationEventPublisher;
    protected final ContractRepository contractRepository;

    @Override
    public void write(Chunk<? extends ContractEntity> chunk) {
        chunk.forEach(contractEntity -> {
            changeStatus(contractEntity);

            contractRepository.saveContract(contractEntity);

            publishEvent(contractEntity);
        });
    }

    protected abstract void changeStatus(ContractEntity contractEntity);

    protected abstract void publishEvent(ContractEntity contractEntity);
}
