package com.example.contractservice.contract.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ContractJobConfig {
    public static final String CONTRACT_STATUS_CHANGE_JOB_NAME = "statusChangeJob";

    private final JobRepository jobRepository;

    private final Step contractToDoneBatchStep;
    private final Step contractToInProgressBatchStep;
    private final Step contractToCancelledBatchStep;

    @Bean
    public Job statusChangeJob() {
        return new JobBuilder(CONTRACT_STATUS_CHANGE_JOB_NAME, jobRepository)
                .start(contractToDoneBatchStep)
                .next(contractToInProgressBatchStep)
                .next(contractToCancelledBatchStep)
                .build();
    }
}
