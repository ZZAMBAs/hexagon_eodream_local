package com.example.contractservice.contract.controller;

import static com.example.contractservice.contract.common.config.ContractJobConfig.CONTRACT_STATUS_CHANGE_JOB_NAME;
import static java.time.ZoneOffset.UTC;

import com.example.contractservice.common.controller.dto.BatchJobInfo;
import com.example.contractservice.common.util.StringUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hexagon.core.dto.ResponseDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contracts/batch")
public class ContractBatchController {

    private final Job statusChangeJob;
    private final JobLauncher asyncJobLauncher;
    private final JobExplorer jobExplorer;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<BatchJobInfo> processManually(@RequestParam("date") LocalDate date)
            throws JobExecutionException {
        JobExecution jobExecution = asyncJobLauncher.run(statusChangeJob, jobParameters(date));

        return getSuccessJobInfo(jobExecution);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<BatchJobInfo> getRunningJobInfo(
            @RequestParam(value = "date", required = false) LocalDate date,
            @RequestParam(value = "jobInstanceId", required = false) Long jobInstanceId
    ) throws NoSuchJobInstanceException, NoSuchJobExecutionException {
        validateParams(date, jobInstanceId);

        JobInstance jobInstance = getJobInstance(date, jobInstanceId);

        JobExecution lastExecution = getLastExecution(jobInstance);

        return getSuccessJobInfo(lastExecution);
    }

    private void validateParams(LocalDate date, Long jobInstanceId) {
        if (date == null && jobInstanceId == null) {
            throw new IllegalArgumentException("확인할 날짜 또는 배치 Job Instance id 중 하나를 넣고 요청해야 합니다.");
        }

        if (date != null && jobInstanceId != null) {
            throw new IllegalArgumentException("확인할 날짜 또는 배치 Job Instance id 중 하나만 넣어 요청해야 합니다.");
        }
    }

    private JobInstance getJobInstance(LocalDate date, Long jobInstanceId) throws NoSuchJobInstanceException {
        JobInstance nullableJobInstance;

        if (jobInstanceId != null) {
            nullableJobInstance = jobExplorer.getJobInstance(jobInstanceId);
        } else {
            nullableJobInstance = jobExplorer.getJobInstance(statusChangeJob.getName(), jobParameters(date));
        }

        JobInstance jobInstance = Optional.ofNullable(nullableJobInstance)
                .orElseThrow(() -> new NoSuchJobInstanceException(
                        StringUtil.format("다음 id에 해당하는 JobInstance가 없습니다. id: {}", jobInstanceId)));

        if (!jobInstance.getJobName().equals(CONTRACT_STATUS_CHANGE_JOB_NAME)) {
            throw new NoSuchJobInstanceException(
                    StringUtil.format("다음 id에 해당하는 계약 배치 JobInstance가 없습니다. id: {}", jobInstanceId));
        }

        return jobInstance;
    }

    private JobExecution getLastExecution(JobInstance jobInstance) throws NoSuchJobExecutionException {
        return Optional.ofNullable(jobExplorer.getLastJobExecution(jobInstance)).orElseThrow(
                () -> new NoSuchJobExecutionException(
                        StringUtil.format("다음 JobInstance id에 해당하는 Job 실행 이력이 없습니다. id: {}", jobInstance.getInstanceId())));
    }

    private JobParameters jobParameters(LocalDate date) {
        Instant midnight = date.atStartOfDay(UTC).toInstant();

        return new JobParametersBuilder()
                .addString("dateStr", midnight.toString())
                .toJobParameters();
    }

    private ResponseDto<BatchJobInfo> getSuccessJobInfo(JobExecution jobExecution) {
        return ResponseDto.success(new BatchJobInfo(jobExecution.getJobInstance().getInstanceId(),
                jobExecution.getId(),
                jobExecution.getExitStatus(),
                jobExecution.getStatus()));
    }
}
