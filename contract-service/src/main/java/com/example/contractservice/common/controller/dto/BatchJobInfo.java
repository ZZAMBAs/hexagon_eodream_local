package com.example.contractservice.common.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;

@Schema(description = "Spring Batch Job 실행 정보")
public record BatchJobInfo(
        @Schema(description = "Spring Batch JobInstance ID", example = "1")
        long instanceId,
        @Schema(description = "Spring Batch JobExecution ID", example = "3")
        long executionId,
        @Schema(description = "Spring Batch ExitStatus. 배치 종료 코드와 종료 설명을 포함합니다.")
        ExitStatus exitStatus,
        @Schema(description = "Spring Batch BatchStatus", example = "STARTING", allowableValues = {
                "STARTING", "STARTED", "STOPPING", "STOPPED", "FAILED", "COMPLETED", "ABANDONED", "UNKNOWN"
        })
        BatchStatus status,
        @Schema(description = "해당 JobExecution에서 실행된 Step 목록")
        List<BatchStepInfo> steps) {

}
