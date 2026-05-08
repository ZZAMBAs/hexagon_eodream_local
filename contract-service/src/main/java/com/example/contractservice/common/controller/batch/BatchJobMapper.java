package com.example.contractservice.common.controller.batch;

import com.example.contractservice.common.controller.dto.BatchJobInfo;
import com.example.contractservice.common.controller.dto.BatchStepInfo;
import java.util.Comparator;
import java.util.List;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

@Component
public class BatchJobMapper {

    public BatchJobInfo toInfo(JobExecution jobExecution) {
        return new BatchJobInfo(
                jobExecution.getJobInstance().getInstanceId(),
                jobExecution.getId(),
                jobExecution.getExitStatus(),
                jobExecution.getStatus(),
                toStepInfos(jobExecution)
        );
    }

    private List<BatchStepInfo> toStepInfos(JobExecution jobExecution) {
        return jobExecution.getStepExecutions().stream()
                .sorted(Comparator.comparing(StepExecution::getStartTime,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .map(this::toStepInfo)
                .toList();
    }

    private BatchStepInfo toStepInfo(StepExecution stepExecution) {
        return new BatchStepInfo(
                stepExecution.getStepName(),
                stepExecution.getStatus(),
                stepExecution.getExitStatus(),
                stepExecution.getReadCount(),
                stepExecution.getWriteCount(),
                stepExecution.getCommitCount(),
                stepExecution.getRollbackCount(),
                stepExecution.getReadSkipCount(),
                stepExecution.getProcessSkipCount(),
                stepExecution.getWriteSkipCount(),
                stepExecution.getStartTime(),
                stepExecution.getEndTime()
        );
    }
}
