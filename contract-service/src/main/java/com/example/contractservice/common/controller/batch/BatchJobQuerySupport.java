package com.example.contractservice.common.controller.batch;

import com.example.contractservice.common.util.StringUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchJobQuerySupport {

    private final JobExplorer jobExplorer;

    public void validateSingleSearchParameter(Object date, Long jobInstanceId) {
        if (date == null && jobInstanceId == null) {
            throw new IllegalArgumentException("확인할 날짜 또는 배치 Job Instance id 중 하나를 넣고 요청해야 합니다.");
        }

        if (date != null && jobInstanceId != null) {
            throw new IllegalArgumentException("확인할 날짜 또는 배치 Job Instance id 중 하나만 넣어 요청해야 합니다.");
        }
    }

    public JobExecution getLastExecution(String jobName, JobParameters jobParameters, Long jobInstanceId)
            throws NoSuchJobInstanceException, NoSuchJobExecutionException {
        JobInstance jobInstance = getJobInstance(jobName, jobParameters, jobInstanceId);

        return Optional.ofNullable(jobExplorer.getLastJobExecution(jobInstance)).orElseThrow(
                () -> new NoSuchJobExecutionException(
                        StringUtil.format("다음 JobInstance id에 해당하는 Job 실행 이력이 없습니다. id: {}",
                                jobInstance.getInstanceId())));
    }

    private JobInstance getJobInstance(String jobName, JobParameters jobParameters, Long jobInstanceId)
            throws NoSuchJobInstanceException {
        JobInstance nullableJobInstance;

        if (jobInstanceId != null) {
            nullableJobInstance = jobExplorer.getJobInstance(jobInstanceId);
        } else {
            nullableJobInstance = jobExplorer.getJobInstance(jobName, jobParameters);
        }

        JobInstance jobInstance = Optional.ofNullable(nullableJobInstance)
                .orElseThrow(() -> new NoSuchJobInstanceException(
                        StringUtil.format("다음 조건에 해당하는 JobInstance가 없습니다. {}", searchCondition(jobName,
                                jobParameters,
                                jobInstanceId))));

        if (!jobInstance.getJobName().equals(jobName)) {
            throw new NoSuchJobInstanceException(
                    StringUtil.format("다음 id에 해당하는 {} JobInstance가 없습니다. id: {}", jobName, jobInstanceId));
        }

        return jobInstance;
    }

    private String searchCondition(String jobName, JobParameters jobParameters, Long jobInstanceId) {
        if (jobInstanceId != null) {
            return StringUtil.format("id: {}", jobInstanceId);
        }

        return StringUtil.format("jobName: {}, parameters: {}", jobName, jobParameters);
    }
}
