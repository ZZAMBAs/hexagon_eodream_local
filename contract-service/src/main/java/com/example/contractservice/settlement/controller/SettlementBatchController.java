package com.example.contractservice.settlement.controller;

import static com.example.contractservice.settlement.common.config.SettlementJobConfig.SETTLEMENT_JOB_NAME;

import com.example.contractservice.common.controller.batch.BatchJobMapper;
import com.example.contractservice.common.controller.batch.BatchJobQuerySupport;
import com.example.contractservice.common.controller.dto.BatchJobInfo;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.hexagon.core.dto.ResponseDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
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
@RequestMapping("/api/settlement/batch")
public class SettlementBatchController {

    private final Job settlementJob;
    private final JobLauncher asyncJobLauncher;
    private final BatchJobQuerySupport batchJobQuerySupport;
    private final BatchJobMapper batchJobMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<BatchJobInfo> processManually(@RequestParam("date") LocalDate date)
            throws JobExecutionException {
        JobExecution jobExecution = asyncJobLauncher.run(settlementJob, jobParameters(date));

        return ResponseDto.success(batchJobMapper.toInfo(jobExecution));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<BatchJobInfo> getRunningJobInfo(
            @RequestParam(value = "date", required = false) LocalDate date,
            @RequestParam(value = "jobInstanceId", required = false) Long jobInstanceId
    ) throws NoSuchJobInstanceException, NoSuchJobExecutionException {
        batchJobQuerySupport.validateSingleSearchParameter(date, jobInstanceId);

        JobExecution lastExecution = batchJobQuerySupport.getLastExecution(
                SETTLEMENT_JOB_NAME,
                date == null ? null : jobParameters(date),
                jobInstanceId
        );

        return ResponseDto.success(batchJobMapper.toInfo(lastExecution));
    }

    private JobParameters jobParameters(LocalDate date) {
        return new JobParametersBuilder()
                .addLocalDate("dateStr", date)
                .toJobParameters();
    }
}
