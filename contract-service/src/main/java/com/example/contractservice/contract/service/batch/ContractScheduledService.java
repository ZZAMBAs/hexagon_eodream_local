package com.example.contractservice.contract.service.batch;

import static java.time.ZoneOffset.UTC;

import java.time.Instant;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
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
    public void changeStatus() {
        LocalDate curDate = Instant.now().atZone(UTC).toLocalDate(); // Instant -> LocalDate(년-월-일)
        Instant midnight = curDate.atStartOfDay(UTC).toInstant(); // LocalDate(년-월-일) 자정 -> Instant

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("dateStr", midnight.toString())
                .toJobParameters();

        log.info("현재 날짜: {}", midnight);

        try {
            jobLauncher.run(statusChangeJob, jobParameters);
        } catch (Exception e) {
            log.warn("정산 배치 중 오류가 발생했습니다.", e);
        }

    }

}
