package com.example.contractservice.deposit.service.batch;

import com.example.contractservice.deposit.domain.Deposit;
import com.example.contractservice.deposit.domain.DepositHistory;
import com.example.contractservice.deposit.domain.DepositPending;
import com.example.contractservice.deposit.domain.vo.DepositChange;
import com.example.contractservice.deposit.repository.DepositPendingRepository;
import com.example.contractservice.deposit.repository.DepositRepository;
import com.example.contractservice.deposit.repository.batch.DepositBatchRepository;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositPendingChunkService {

    private static final DateTimeFormatter SUMMARY_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DepositPendingRepository depositPendingRepository;
    private final DepositRepository depositRepository;
    private final DepositBatchRepository depositBatchRepository;

    @Value("${admin.member.code}")
    private String adminMemberCode;

    @Value("${batch.deposit-pending.size}")
    private int batchSize;

    @Transactional
    public ChunkResult processChunk(Instant cutoff) {
        List<DepositPending> depositPendings = depositPendingRepository.findPendingBeforeWithLock(cutoff, batchSize);

        if (depositPendings.isEmpty()) {
            log.info("[deposit-pending-batch] no due rows in current chunk. cutoff={}", cutoff);
            return ChunkResult.empty();
        }

        Deposit adminDeposit = depositRepository.findDepositByMemberCodeForUpdate(adminMemberCode);
        Instant processedAt = Instant.now();

        int successCount = 0;
        int failedCount = 0;
        List<Long> completedIds = new ArrayList<>();
        List<Long> failedIds = new ArrayList<>();
        List<DepositHistory> depositHistories = new ArrayList<>();

        for (DepositPending depositPending : depositPendings) {
            if (depositPending.isInvalidAmount()) {
                DepositPending failedPending = depositPending.fail(processedAt);
                failedIds.add(failedPending.getId());
                failedCount++;
                log.warn("[deposit-pending-batch] invalid pending amount. id={}, contractCode={}, amount={}",
                        failedPending.getId(), failedPending.getContractCode(), failedPending.getAmount());
                continue;
            }

            DepositPending completedPending = depositPending.complete(processedAt);
            completedIds.add(completedPending.getId());
            successCount++;

            if (!completedPending.hasPositiveAmount()) {
                continue;
            }

            adminDeposit.transfer(completedPending.getAmount());
            depositHistories.add(new DepositHistory(
                    adminDeposit.getCode(),
                    completedPending.getContractCode(),
                    new DepositChange(completedPending.getAmount(), adminDeposit.getAmount()),
                    buildSummary(completedPending.getCreatedAt())
            ));
        }

        if (!depositHistories.isEmpty()) {
            depositBatchRepository.updateAllDeposits(Map.of(adminMemberCode, adminDeposit));
            depositBatchRepository.saveAllHistories(depositHistories);
        }

        if (!completedIds.isEmpty()) {
            depositPendingRepository.markCompletedByIds(completedIds, processedAt);
        }
        if (!failedIds.isEmpty()) {
            depositPendingRepository.markFailedByIds(failedIds, processedAt);
        }

        log.info("[deposit-pending-batch] chunk processed. readCount={}, successCount={}, failedCount={}",
                depositPendings.size(), successCount, failedCount);

        return new ChunkResult(depositPendings.size(), successCount, failedCount);
    }

    private String buildSummary(Instant createdAt) {
        return createdAt.atOffset(ZoneOffset.UTC).toLocalDate().format(SUMMARY_DATE_FORMATTER) + " 결제 건 배치 입금";
    }

    public record ChunkResult(
            int readCount,
            int successCount,
            int failedCount
    ) {
        public static ChunkResult empty() {
            return new ChunkResult(0, 0, 0);
        }
    }
}
