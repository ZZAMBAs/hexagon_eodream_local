package com.example.contractservice.settlement.common.config;

import com.example.contractservice.common.util.StringUtil;
import com.example.contractservice.settlement.domain.Settlement;
import com.example.contractservice.settlement.entity.SettlementEntity;
import com.example.contractservice.settlement.service.batch.processor.SettlementDataProcessor;
import com.example.contractservice.settlement.service.batch.reader.SettlementZeroOffsetItemReader;
import com.example.contractservice.settlement.service.batch.writer.SettlementCustomWriter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SettlementStepConfig {
    private static final int RETRY_LIMIT = 3;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SettlementZeroOffsetItemReader settlementZeroOffsetItemReader;
    private final SettlementDataProcessor settlementDataProcessor;
    private final SettlementCustomWriter settlementCustomWriter;

    @Value("${batch.settlement.size}")
    private int batchSize;

    @Bean
    public Step processSettlementStep() {
        return new StepBuilder("processSettlementStep", jobRepository)
                .<SettlementEntity, Settlement>chunk(batchSize, transactionManager)
                .reader(settlementZeroOffsetItemReader)
                .processor(settlementDataProcessor)
                .writer(settlementCustomWriter)
                .faultTolerant()
                .retry(OptimisticLockingFailureException.class)
                .retry(DataAccessException.class)
                .retryLimit(RETRY_LIMIT)
                .backOffPolicy(backOffPolicy())
                .listener(settlementStepExecutionListener())
                .build();
    }

    @Bean
    public BackOffPolicy backOffPolicy() {
        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(2000L);
        policy.setMultiplier(2.0);
        policy.setMaxInterval(10000L);

        return policy;
    }

    @Bean
    public StepExecutionListener settlementStepExecutionListener() {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                StepExecutionListener.super.beforeStep(stepExecution);
            }

            @Override
            public @Nullable ExitStatus afterStep(StepExecution stepExecution) {
                if (stepExecution.getStatus().isUnsuccessful()) {
                    List<Throwable> failureExceptions = stepExecution.getFailureExceptions();
                    String exceptionSummary = failureExceptions.stream().map(Throwable::toString).collect(Collectors.joining(", "));

                    log.error(StringUtil.format("정산 배치 처리가 정상 종료되지 않았습니다. 발생한 예외 목록: {}", exceptionSummary),
                            failureExceptions.isEmpty() ? null : failureExceptions.get(0));
                }

                return StepExecutionListener.super.afterStep(stepExecution);
            }
        };
    }

}
