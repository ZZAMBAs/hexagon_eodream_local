package com.example.contractservice.contract.controller;

import static com.example.contractservice.contract.common.config.ContractJobConfig.CONTRACT_STATUS_CHANGE_JOB_NAME;
import static java.time.ZoneOffset.UTC;

import com.example.contractservice.common.controller.batch.BatchJobMapper;
import com.example.contractservice.common.controller.batch.BatchJobQuerySupport;
import com.example.contractservice.common.controller.dto.BatchJobInfo;
import com.example.contractservice.contract.common.swagger.annotation.GetContractBatchInfoApi;
import com.example.contractservice.contract.common.swagger.annotation.RunContractBatchApi;
import java.time.Instant;
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
@RequestMapping("/api/contracts/batch")
public class ContractBatchController {

    private final Job statusChangeJob;
    private final JobLauncher asyncJobLauncher;
    private final BatchJobQuerySupport batchJobQuerySupport;
    private final BatchJobMapper batchJobMapper;

    @RunContractBatchApi
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<BatchJobInfo> processManually(@RequestParam("date") LocalDate date)
            throws JobExecutionException {
        JobExecution jobExecution = asyncJobLauncher.run(statusChangeJob, jobParameters(date));

        return ResponseDto.success(batchJobMapper.toInfo(jobExecution));
    }

    @GetContractBatchInfoApi
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<BatchJobInfo> getRunningJobInfo(
            @RequestParam(value = "date", required = false) LocalDate date,
            @RequestParam(value = "jobInstanceId", required = false) Long jobInstanceId
    ) throws NoSuchJobInstanceException, NoSuchJobExecutionException {
        batchJobQuerySupport.validateSingleSearchParameter(date, jobInstanceId);

        JobExecution lastExecution = batchJobQuerySupport.getLastExecution(
                CONTRACT_STATUS_CHANGE_JOB_NAME,
                date == null ? null : jobParameters(date),
                jobInstanceId
        );

        return ResponseDto.success(batchJobMapper.toInfo(lastExecution));
    }

    private JobParameters jobParameters(LocalDate date) {
        Instant midnight = date.atStartOfDay(UTC).toInstant();

        return new JobParametersBuilder()
                .addString("dateStr", midnight.toString())
                .toJobParameters();
    }
}
