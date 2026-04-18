package com.example.contractservice.common.controller.dto;

import java.time.LocalDateTime;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;

public record BatchStepInfo(String stepName,
                            BatchStatus status,
                            ExitStatus exitStatus,
                            long readCount,
                            long writeCount,
                            long commitCount,
                            long rollbackCount,
                            long readSkipCount,
                            long processSkipCount,
                            long writeSkipCount,
                            LocalDateTime startTime,
                            LocalDateTime endTime) {

}
