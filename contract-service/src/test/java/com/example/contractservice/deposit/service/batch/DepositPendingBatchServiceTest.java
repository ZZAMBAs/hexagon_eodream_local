package com.example.contractservice.deposit.service.batch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class DepositPendingBatchServiceTest {

    @Mock
    private DepositPendingChunkService depositPendingChunkService;

    private DepositPendingBatchService depositPendingBatchService;

    @BeforeEach
    void setUp() {
        depositPendingBatchService = new DepositPendingBatchService(depositPendingChunkService);
        ReflectionTestUtils.setField(depositPendingBatchService, "batchSize", 500);
    }

    @Test
    @DisplayName("DB 일시 오류는 chunk 단위로 최대 3회까지 재시도한다")
    void success_retry_chunk_on_data_access_exception() {
        when(depositPendingChunkService.processChunk(any()))
                .thenThrow(new RecoverableDataAccessException("retry-1"))
                .thenThrow(new RecoverableDataAccessException("retry-2"))
                .thenReturn(DepositPendingChunkService.ChunkResult.empty());

        depositPendingBatchService.processDailyDepositPendings();

        verify(depositPendingChunkService, times(3)).processChunk(any());
    }
}
