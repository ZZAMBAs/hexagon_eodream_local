package com.example.contractservice.settlement.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementScheduledService {

    private final JobLauncher jobLauncher;
    private final Job settlementJob;

    @Scheduled(cron = "${batch.settlement.interval}")
    public void batchProcessSettlement() throws JobExecutionException {
        log.info("정산 스케줄링을 시작합니다.");

        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDate("dateStr", LocalDate.now())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(settlementJob, jobParameters);

        if (log.isDebugEnabled()) {
            log.debug("스케줄링된 JobInstanceId: {}, 상태: {}", jobExecution.getJobInstance().getInstanceId(),
                    jobExecution.getStatus());
        }
    }
}
