package com.example.contractservice.settlement.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class SettlementJobConfig {
    public static final String SETTLEMENT_JOB_NAME = "settlementJob";

    private final JobRepository jobRepository;

    @Bean
    public JobLauncher asyncJobLauncher() {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setTaskExecutor(asyncTaskExecutor());
        jobLauncher.setJobRepository(jobRepository);

        return jobLauncher;
    }

    @Bean
    public ThreadPoolTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        threadPoolTaskExecutor.setCorePoolSize(1);
        threadPoolTaskExecutor.setMaxPoolSize(2); // 서로 다른 날짜 병렬 실행 허용 (스케줄링 + 수동 API)
        threadPoolTaskExecutor.setQueueCapacity(0); // 모든 스레드 사용 시, 요청 거절
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);

        return threadPoolTaskExecutor;
    }

    @Bean
    public Job settlementJob(Step processSettlementStep) {
        return new JobBuilder(SETTLEMENT_JOB_NAME, jobRepository)
                .start(processSettlementStep)
                .build();
    }
}
