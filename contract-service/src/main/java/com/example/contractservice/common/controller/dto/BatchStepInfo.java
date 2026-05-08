package com.example.contractservice.common.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;

@Schema(description = "Spring Batch Step 실행 정보")
public record BatchStepInfo(
        @Schema(description = "Spring Batch Step 이름", example = "processSettlementStep")
        String stepName,
        @Schema(description = "Spring Batch Step BatchStatus", example = "COMPLETED", allowableValues = {
                "STARTING", "STARTED", "STOPPING", "STOPPED", "FAILED", "COMPLETED", "ABANDONED", "UNKNOWN"
        })
        BatchStatus status,
        @Schema(description = "Spring Batch Step ExitStatus. Step 종료 코드와 종료 설명을 포함합니다.")
        ExitStatus exitStatus,
        @Schema(description = "Reader가 읽은 item 수", example = "500")
        long readCount,
        @Schema(description = "Writer가 기록한 item 수", example = "500")
        long writeCount,
        @Schema(description = "Step에서 커밋된 청크 트랜잭션 수", example = "1")
        long commitCount,
        @Schema(description = "Step에서 롤백된 청크 트랜잭션 수", example = "0")
        long rollbackCount,
        @Schema(description = "Reader 단계에서 skip된 item 수", example = "0")
        long readSkipCount,
        @Schema(description = "Processor 단계에서 skip된 item 수", example = "0")
        long processSkipCount,
        @Schema(description = "Writer 단계에서 skip된 item 수", example = "0")
        long writeSkipCount,
        @Schema(description = "Step 시작 일시. Spring Batch 메타데이터의 LocalDateTime 값입니다.", example = "2026-04-18T00:00:00")
        LocalDateTime startTime,
        @Schema(description = "Step 종료 일시. Spring Batch 메타데이터의 LocalDateTime 값이며, 아직 종료되지 않았다면 null입니다.", example = "2026-04-18T00:00:03")
        LocalDateTime endTime) {

}
