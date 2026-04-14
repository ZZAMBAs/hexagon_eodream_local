package com.example.contractservice.settlement.service.batch;

import com.example.contractservice.deposit.entity.DepositEntity;
import com.example.contractservice.deposit.repository.DepositJpaRepository;
import com.example.contractservice.settlement.common.SettlementStatus;
import com.example.contractservice.settlement.entity.SettlementEntity;
import com.example.contractservice.settlement.repository.SettlementJpaRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class SettlementBatchTestFixture {

    static final LocalDate TARGET_DATE = LocalDate.of(2026, 4, 15);
    static final long ADMIN_INITIAL_AMOUNT = 1_000_000L;
    static final long RECEIVER_INITIAL_AMOUNT = 10_000L;
    static final int VALID_BEFORE_COUNT = 7;

    private final SettlementJpaRepository settlementJpaRepository;
    private final DepositJpaRepository depositJpaRepository;
    private final String adminMemberCode;

    SettlementBatchTestFixture(SettlementJpaRepository settlementJpaRepository,
            DepositJpaRepository depositJpaRepository,
            String adminMemberCode) {
        this.settlementJpaRepository = settlementJpaRepository;
        this.depositJpaRepository = depositJpaRepository;
        this.adminMemberCode = adminMemberCode;
    }

    SettlementBatchFixtureData create() {
        Map<String, DepositEntity> receiverDeposits = createDeposits();
        List<SettlementEntity> validBeforeSettlements = createValidBeforeSettlements(receiverDeposits);

        return new SettlementBatchFixtureData(
                receiverDeposits,
                validBeforeSettlements,
                saveSettlement(receiverCode(1, receiverDeposits), -1L, SettlementStatus.BEFORE,
                        TARGET_DATE.minusDays(1)),
                saveSettlement(receiverCode(0, receiverDeposits), 20_000L, SettlementStatus.DONE,
                        TARGET_DATE.minusDays(2)),
                saveSettlement(receiverCode(1, receiverDeposits), 30_000L, SettlementStatus.FAILED,
                        TARGET_DATE.minusDays(3)),
                saveSettlement(receiverCode(2, receiverDeposits), 40_000L, SettlementStatus.BEFORE,
                        TARGET_DATE.minusMonths(1).minusDays(1)),
                saveSettlement(receiverCode(2, receiverDeposits), 50_000L, SettlementStatus.BEFORE,
                        TARGET_DATE)
        );
    }

    private Map<String, DepositEntity> createDeposits() {
        DepositEntity adminDeposit = DepositEntity.createBy(adminMemberCode);
        adminDeposit.updateInfo(ADMIN_INITIAL_AMOUNT);
        depositJpaRepository.save(adminDeposit);

        Map<String, DepositEntity> receiverDeposits = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            String receiverCode = "receiver-" + i;
            DepositEntity receiverDeposit = DepositEntity.createBy(receiverCode);
            receiverDeposit.updateInfo(RECEIVER_INITIAL_AMOUNT);
            receiverDeposits.put(receiverCode, depositJpaRepository.save(receiverDeposit));
        }

        return receiverDeposits;
    }

    private List<SettlementEntity> createValidBeforeSettlements(Map<String, DepositEntity> receiverDeposits) {
        List<SettlementEntity> settlements = new ArrayList<>();

        for (int i = 0; i < VALID_BEFORE_COUNT; i++) {
            settlements.add(saveSettlement(
                    receiverCode(i, receiverDeposits),
                    10_000L + i,
                    SettlementStatus.BEFORE,
                    TARGET_DATE.minusDays((i % 10) + 1)));
        }

        return settlements;
    }

    private SettlementEntity saveSettlement(String receiverCode, Long originalAmount, SettlementStatus status,
            LocalDate progressingAt) {
        return settlementJpaRepository.save(SettlementEntity.builder()
                .code(UUID.randomUUID().toString())
                .receiverCode(receiverCode)
                .contractCode(UUID.randomUUID().toString())
                .originalAmount(originalAmount)
                .status(status)
                .progressingAt(progressingAt)
                .createdAt(Instant.now())
                .build());
    }

    private String receiverCode(int index, Map<String, DepositEntity> receiverDeposits) {
        return "receiver-" + (index % receiverDeposits.size());
    }
}

record SettlementBatchFixtureData(
        Map<String, DepositEntity> receiverDeposits,
        List<SettlementEntity> validBeforeSettlements,
        SettlementEntity invalidBeforeSettlement,
        SettlementEntity doneSettlement,
        SettlementEntity failedSettlement,
        SettlementEntity outOfRangeSettlement,
        SettlementEntity endBoundarySettlement
) {
}
