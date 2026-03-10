package com.example.contractservice.deposit.service.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.contractservice.deposit.common.DepositPendingStatus;
import com.example.contractservice.deposit.domain.Deposit;
import com.example.contractservice.deposit.domain.DepositHistory;
import com.example.contractservice.deposit.domain.DepositPending;
import com.example.contractservice.deposit.domain.vo.DepositPendingDetails;
import com.example.contractservice.deposit.repository.DepositPendingRepository;
import com.example.contractservice.deposit.repository.DepositRepository;
import com.example.contractservice.deposit.repository.batch.DepositBatchRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class DepositPendingChunkServiceTest {

    @Mock
    private DepositPendingRepository depositPendingRepository;
    @Mock
    private DepositRepository depositRepository;
    @Mock
    private DepositBatchRepository depositBatchRepository;

    private DepositPendingChunkService depositPendingChunkService;

    @BeforeEach
    void setUp() {
        depositPendingChunkService = new DepositPendingChunkService(
                depositPendingRepository,
                depositRepository,
                depositBatchRepository
        );
        ReflectionTestUtils.setField(depositPendingChunkService, "adminMemberCode", "admin-code");
        ReflectionTestUtils.setField(depositPendingChunkService, "batchSize", 500);
    }

    @Test
    @DisplayName("금액이 양수인 DepositPending 데이터는 관리자 예치금과 히스토리에 반영되고 COMPLETED 처리된다")
    void success_process_chunk_completes_positive_rows() {
        DepositPending pending1 = createPending(1L, "contract-1", 100L, Instant.parse("2026-02-20T00:00:00Z"));
        DepositPending pending2 = createPending(2L, "contract-2", 200L, Instant.parse("2026-02-21T00:00:00Z"));
        Deposit adminDeposit = new Deposit("deposit-code", Instant.now(), Instant.now(), "admin-code", 1000L);

        when(depositPendingRepository.findPendingBeforeWithLock(any(), eq(500))).thenReturn(List.of(pending1, pending2));
        when(depositRepository.findDepositByMemberCodeForUpdate("admin-code")).thenReturn(adminDeposit);

        DepositPendingChunkService.ChunkResult result = depositPendingChunkService.processChunk(Instant.now().minusSeconds(1));

        assertEquals(2, result.readCount());
        assertEquals(2, result.successCount());
        assertEquals(0, result.failedCount());
        assertEquals(DepositPendingStatus.PENDING, pending1.getStatus());
        assertEquals(DepositPendingStatus.PENDING, pending2.getStatus());

        ArgumentCaptor<Map<String, Deposit>> depositMapCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<List<DepositHistory>> historiesCaptor = ArgumentCaptor.forClass(List.class);
        verify(depositBatchRepository).updateAllDeposits(depositMapCaptor.capture());
        assertEquals(1300L, depositMapCaptor.getValue().get("admin-code").getAmount());
        verify(depositBatchRepository).saveAllHistories(historiesCaptor.capture());
        assertEquals("2026-02-20 결제 건 배치 입금", historiesCaptor.getValue().get(0).getSummary());
        verify(depositPendingRepository).markCompletedByIds(eq(List.of(1L, 2L)), any());
        verify(depositPendingRepository, never()).markFailedByIds(any(), any());
    }

    @Test
    @DisplayName("금액이 0원인 DepositPending 데이터는 COMPLETED 처리되지만 히스토리 기록은 생략된다")
    void success_complete_zero_amount_without_history() {
        DepositPending pending = createPending(1L, "contract-1", 0L, Instant.parse("2026-02-20T00:00:00Z"));
        Deposit adminDeposit = new Deposit("deposit-code", Instant.now(), Instant.now(), "admin-code", 1000L);

        when(depositPendingRepository.findPendingBeforeWithLock(any(), eq(500))).thenReturn(List.of(pending));
        when(depositRepository.findDepositByMemberCodeForUpdate("admin-code")).thenReturn(adminDeposit);

        DepositPendingChunkService.ChunkResult result = depositPendingChunkService.processChunk(Instant.now().minusSeconds(1));

        assertEquals(1, result.successCount());
        assertEquals(0, result.failedCount());
        assertEquals(DepositPendingStatus.PENDING, pending.getStatus());
        verify(depositBatchRepository, never()).updateAllDeposits(any());
        verify(depositBatchRepository, never()).saveAllHistories(any());
        verify(depositPendingRepository).markCompletedByIds(eq(List.of(1L)), any());
        verify(depositPendingRepository, never()).markFailedByIds(any(), any());
    }

    @Test
    @DisplayName("금액이 음수인 DepositPending 데이터는 FAILED 처리된다")
    void success_mark_failed_for_negative_amount() {
        DepositPending pending = createPending(1L, "contract-1", -100L, Instant.parse("2026-02-20T00:00:00Z"));
        Deposit adminDeposit = new Deposit("deposit-code", Instant.now(), Instant.now(), "admin-code", 1000L);

        when(depositPendingRepository.findPendingBeforeWithLock(any(), eq(500))).thenReturn(List.of(pending));
        when(depositRepository.findDepositByMemberCodeForUpdate("admin-code")).thenReturn(adminDeposit);

        DepositPendingChunkService.ChunkResult result = depositPendingChunkService.processChunk(Instant.now().minusSeconds(1));

        assertEquals(0, result.successCount());
        assertEquals(1, result.failedCount());
        assertEquals(DepositPendingStatus.PENDING, pending.getStatus());
        verify(depositBatchRepository, never()).updateAllDeposits(any());
        verify(depositBatchRepository, never()).saveAllHistories(any());
        verify(depositPendingRepository, never()).markCompletedByIds(any(), any());
        verify(depositPendingRepository).markFailedByIds(eq(List.of(1L)), any());
    }

    private DepositPending createPending(Long id, String contractCode, Long amount, Instant createdAt) {
        return new DepositPending(
                id,
                createdAt,
                createdAt,
                contractCode,
                new DepositPendingDetails(amount, DepositPendingStatus.PENDING, null)
        );
    }
}
