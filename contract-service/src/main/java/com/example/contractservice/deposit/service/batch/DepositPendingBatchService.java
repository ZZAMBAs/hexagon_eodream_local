package com.example.contractservice.deposit.service.batch;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositPendingBatchService {

    private static final int MAX_CHUNK_RETRY_COUNT = 3;
    private static final long[] RETRY_DELAY_JITTER_MILLIS = {1000L, 3000L, 5000L};

    private final DepositPendingChunkService depositPendingChunkService;

    @Value("${batch.deposit-pending.size}")
    private int batchSize;

    @Scheduled(cron = "${batch.deposit-pending.interval}", zone = "UTC")
    public void processDailyDepositPendings() {
        Instant startedAt = Instant.now();
        Instant executionMidnight = LocalDate.now(ZoneOffset.UTC)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);
        Instant cutoff = executionMidnight.minus(7, ChronoUnit.DAYS);

        int totalRead = 0;
        int totalSuccess = 0;
        int totalFailed = 0;
        int chunkCount = 0;

        log.info("[deposit-pending-batch] scheduler triggered. executionMidnight={}, cutoff={}, batchSize={}",
                executionMidnight, cutoff, batchSize);

        while (true) {
            DepositPendingChunkService.ChunkResult chunkResult;

            try {
                chunkResult = processChunkWithRetry(cutoff, chunkCount);
            } catch (RuntimeException e) {
                log.error("[deposit-pending-batch] aborted. chunkIndex={}, totalRead={}, totalSuccess={}, totalFailed={}",
                        chunkCount, totalRead, totalSuccess, totalFailed, e);
                break;
            }

            if (chunkResult.readCount() == 0) {
                break;
            }

            chunkCount++;
            totalRead += chunkResult.readCount();
            totalSuccess += chunkResult.successCount();
            totalFailed += chunkResult.failedCount();

            if (chunkResult.readCount() < batchSize) {
                break;
            }
        }

        log.info("[deposit-pending-batch] end. chunkCount={}, totalRead={}, totalSuccess={}, totalFailed={}, durationMs={}",
                chunkCount, totalRead, totalSuccess, totalFailed, Duration.between(startedAt, Instant.now()).toMillis());
    }

    private DepositPendingChunkService.ChunkResult processChunkWithRetry(
            Instant cutoff,
            int chunkIndex
    ) {
        for (int attempt = 1; attempt <= MAX_CHUNK_RETRY_COUNT; attempt++) {
            try {
                return depositPendingChunkService.processChunk(cutoff);
            } catch (RuntimeException e) {
                if (!isRetryable(e) || attempt == MAX_CHUNK_RETRY_COUNT) {
                    throw e;
                }

                log.warn("[deposit-pending-batch] chunk retry. chunkIndex={}, attempt={}/{}",
                        chunkIndex, attempt, MAX_CHUNK_RETRY_COUNT, e);
                sleep(RETRY_DELAY_JITTER_MILLIS[attempt - 1]);
            }
        }

        throw new IllegalStateException("Unreachable batch retry state");
    }

    private boolean isRetryable(RuntimeException exception) {
        return exception instanceof DataAccessException;
    }

    private void sleep(long delayMillis) {
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Deposit pending batch retry interrupted", e);
        }
    }
}
