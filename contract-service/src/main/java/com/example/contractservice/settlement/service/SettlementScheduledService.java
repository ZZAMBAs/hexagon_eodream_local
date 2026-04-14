package com.example.contractservice.settlement.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
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
    public void batchProcessSettlement() {
        log.info("정산 스케줄링을 시작합니다.");
        try {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDate("dateStr", LocalDate.now())
                .toJobParameters();

            jobLauncher.run(settlementJob, jobParameters);
        } catch (Exception e) {
            log.error("정산 배치 실행에 문제가 발생했습니다. 배치를 수행할 수 없습니다.", e);
        }
    }
}
