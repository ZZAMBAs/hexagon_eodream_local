package com.example.contractservice.deposit.domain;

import static com.example.contractservice.deposit.domain.exception.DepositErrorCode.INVALID_PENDING_STATUS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.contractservice.deposit.common.DepositPendingStatus;
import com.example.contractservice.deposit.domain.exception.DepositException;
import com.example.contractservice.deposit.domain.vo.DepositPendingDetails;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DepositPendingTest {

    @Test
    @DisplayName("PENDING 상태가 아닌 DepositPending 데이터는 complete 할 수 없다")
    void fail_complete_when_status_is_not_pending() {
        DepositPending depositPending = new DepositPending(
                1L,
                "contract-code",
                new DepositPendingDetails(100L, DepositPendingStatus.COMPLETED, Instant.now())
        );

        DepositException exception = assertThrows(DepositException.class,
                () -> depositPending.complete(Instant.now()));

        assertEquals(INVALID_PENDING_STATUS, exception.getErrorCode());
    }

    @Test
    @DisplayName("PENDING 상태가 아닌 DepositPending 데이터는 fail 할 수 없다")
    void fail_fail_when_status_is_not_pending() {
        DepositPending depositPending = new DepositPending(
                1L,
                "contract-code",
                new DepositPendingDetails(100L, DepositPendingStatus.FAILED, Instant.now())
        );

        DepositException exception = assertThrows(DepositException.class,
                () -> depositPending.fail(Instant.now()));

        assertEquals(INVALID_PENDING_STATUS, exception.getErrorCode());
    }
}
