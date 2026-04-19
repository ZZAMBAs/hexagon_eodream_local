package com.example.contractservice.contract.service.batch.writer;

import com.example.contractservice.contract.domain.Contract;
import com.example.contractservice.contract.repository.batch.ContractBatchRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ContractInProgressWriter extends ContractStatusWriter {

    public ContractInProgressWriter(ApplicationEventPublisher applicationEventPublisher,
            ContractBatchRepository contractBatchRepository) {
        super(applicationEventPublisher, contractBatchRepository);
    }

    @Override
    protected void changeStatus(Contract contract) {
        contract.progress();
    }
}
