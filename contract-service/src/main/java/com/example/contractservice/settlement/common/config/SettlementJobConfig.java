package com.example.contractservice.settlement.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SettlementJobConfig {
    private final JobRepository jobRepository;

    @Bean
    public Job settlementJob(Step processSettlementStep) {
        return new JobBuilder("settlementJob", jobRepository)
                .start(processSettlementStep)
                .build();
    }
}
