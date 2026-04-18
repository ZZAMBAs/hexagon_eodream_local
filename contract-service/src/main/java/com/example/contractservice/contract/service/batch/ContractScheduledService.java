package com.example.contractservice.contract.service.batch;

import static java.time.ZoneOffset.UTC;

import java.time.Instant;
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
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContractScheduledService {

    private final JobLauncher jobLauncher;
    private final Job statusChangeJob;

    @Scheduled(cron = "${batch.contract.interval}")
    public void changeStatus() throws JobExecutionException {
        log.info("계약 상태 변경 배치를 시작합니다.");

        LocalDate curDate = Instant.now().atZone(UTC).toLocalDate(); // Instant -> LocalDate(년-월-일)
        Instant midnight = curDate.atStartOfDay(UTC).toInstant(); // LocalDate(년-월-일) 자정 -> Instant

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("dateStr", midnight.toString())
                .toJobParameters();

        log.info("현재 날짜: {}", midnight);

        JobExecution jobExecution = jobLauncher.run(statusChangeJob, jobParameters);

        if (log.isDebugEnabled()) {
            log.debug("스케줄링된 JobInstanceId: {}, 상태: {}", jobExecution.getJobInstance().getInstanceId(),
                    jobExecution.getStatus());
        }
    }

}
