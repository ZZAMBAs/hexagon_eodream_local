package com.example.contractservice.contract.common.config;

import com.example.contractservice.common.batch.listener.FailedStepLoggingListener;
import com.example.contractservice.contract.domain.Contract;
import com.example.contractservice.contract.entity.ContractEntity;
import com.example.contractservice.contract.service.batch.writer.ContractStatusWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ContractStepConfig {
    private static final int RETRY_LIMIT = 3;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final ItemReader<ContractEntity> contractInProgressReader;
    private final ItemReader<ContractEntity> contractPaidReader;
    private final ItemReader<ContractEntity> contractRequestedReader;

    private final ContractStatusWriter contractDoneWriter;
    private final ContractStatusWriter contractInProgressWriter;
    private final ContractStatusWriter contractCancelledWriter;

    @Value("${batch.contract.size}")
    private int chunkSize;

    @Bean
    public Step contractToDoneBatchStep() {
        return contractStep("contractToDoneBatchStep", contractInProgressReader, contractDoneWriter);
    }

    @Bean
    public Step contractToInProgressBatchStep() {
        return contractStep("contractToInProgressBatchStep", contractPaidReader, contractInProgressWriter);
    }

    @Bean
    public Step contractToCancelledBatchStep() {
        return contractStep("contractToCancelledBatchStep", contractRequestedReader, contractCancelledWriter);
    }

    @Bean
    public BackOffPolicy contractBackOffPolicy() {
        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(2000L);
        policy.setMultiplier(2.0);
        policy.setMaxInterval(10000L);

        return policy;
    }

    @Bean
    public StepExecutionListener contractStepExecutionListener() {
        return new FailedStepLoggingListener("계약 상태 변경 배치");
    }

    private Step contractStep(String stepName, ItemReader<ContractEntity> reader, ContractStatusWriter writer) {
        return new StepBuilder(stepName, jobRepository)
                .<ContractEntity, Contract>chunk(chunkSize, transactionManager)
                .reader(reader)
                .writer(writer)
                .faultTolerant()
                .retry(DataAccessException.class)
                .retryLimit(RETRY_LIMIT)
                .backOffPolicy(contractBackOffPolicy())
                .listener(contractStepExecutionListener())
                .build();
    }
}
