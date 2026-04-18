package com.example.contractservice.common.batch.listener;

import com.example.contractservice.common.util.StringUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

@Slf4j
@RequiredArgsConstructor
public class FailedStepLoggingListener implements StepExecutionListener {

    private final String batchName;

    @Override
    public @Nullable ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getStatus().isUnsuccessful()) {
            logFailedStep(stepExecution);
        }

        return StepExecutionListener.super.afterStep(stepExecution);
    }

    private void logFailedStep(StepExecution stepExecution) {
        List<Throwable> failureExceptions = stepExecution.getFailureExceptions();
        String exceptionSummary = failureExceptions.stream()
                .map(Throwable::toString)
                .collect(Collectors.joining(", "));

        String message = StringUtil.format(
                "{} Step이 정상 종료되지 않았습니다. stepName: {}, 발생한 예외 목록: {}",
                batchName,
                stepExecution.getStepName(),
                exceptionSummary.isBlank() ? "없음" : exceptionSummary);

        if (failureExceptions.isEmpty()) {
            log.error(message);
            return;
        }

        log.error(message, failureExceptions.get(0));
    }
}
