package com.example.contractservice.common.controller.dto;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;

public record BatchJobInfo(long instanceId,
                           long executionId,
                           ExitStatus exitStatus,
                           BatchStatus status) {

}
