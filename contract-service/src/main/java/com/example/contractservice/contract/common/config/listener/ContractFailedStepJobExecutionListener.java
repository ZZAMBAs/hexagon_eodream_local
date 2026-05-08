package com.example.contractservice.contract.common.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;

@Slf4j
public class ContractFailedStepJobExecutionListener implements JobExecutionListener {

    @Override
    public void afterJob(JobExecution jobExecution) {
        boolean hasFailedStep = jobExecution.getStepExecutions().stream()
                .anyMatch(stepExecution -> stepExecution.getStatus().isUnsuccessful());

        if (!hasFailedStep) {
            return;
        }

        jobExecution.setStatus(BatchStatus.FAILED);
        jobExecution.setExitStatus(ExitStatus.FAILED);

        String failedSteps = jobExecution.getStepExecutions().stream()
                .filter(stepExecution -> stepExecution.getStatus().isUnsuccessful())
                .map(StepExecution::getStepName)
                .toList()
                .toString();

        log.error("계약 상태 변경 배치 중 실패한 Step이 있습니다. failedSteps: {}", failedSteps);
    }
}
