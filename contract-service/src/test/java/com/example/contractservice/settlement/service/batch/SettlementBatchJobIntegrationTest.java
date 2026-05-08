package com.example.contractservice.settlement.service.batch;

import static com.example.contractservice.settlement.service.batch.SettlementBatchTestFixture.ADMIN_INITIAL_AMOUNT;
import static com.example.contractservice.settlement.service.batch.SettlementBatchTestFixture.RECEIVER_INITIAL_AMOUNT;
import static com.example.contractservice.settlement.service.batch.SettlementBatchTestFixture.TARGET_DATE;
import static com.example.contractservice.settlement.service.batch.SettlementBatchTestFixture.VALID_BEFORE_COUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.contractservice.common.TestConfig;
import com.example.contractservice.deposit.entity.DepositEntity;
import com.example.contractservice.deposit.repository.DepositHistoryJpaRepository;
import com.example.contractservice.deposit.repository.DepositJpaRepository;
import com.example.contractservice.settlement.common.SettlementStatus;
import com.example.contractservice.settlement.domain.Settlement;
import com.example.contractservice.settlement.entity.SettlementEntity;
import com.example.contractservice.settlement.repository.SettlementJpaRepository;
import com.example.contractservice.settlement.service.batch.processor.SettlementDataProcessor;
import com.example.contractservice.settlement.service.batch.reader.SettlementZeroOffsetItemReader;
import com.example.contractservice.settlement.service.batch.writer.SettlementCustomWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@SpringBatchTest
@Import(TestConfig.class)
@ActiveProfiles("batch-test")
class SettlementBatchJobIntegrationTest {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job settlementJob;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private SettlementJpaRepository settlementJpaRepository;
    @Autowired
    private DepositJpaRepository depositJpaRepository;
    @Autowired
    private DepositHistoryJpaRepository depositHistoryJpaRepository;
    @Autowired
    private SettlementZeroOffsetItemReader settlementZeroOffsetItemReader;
    @Autowired
    private SettlementDataProcessor settlementDataProcessor;
    @Autowired
    private SettlementCustomWriter settlementCustomWriter;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Value("${admin.member.code}")
    private String adminMemberCode;
    @Value("${batch.settlement.settlement-rate}")
    private BigDecimal settlementRate;

    @MockitoBean
    private KafkaTemplate<String, String> kafkaTemplate;
    @MockitoBean
    private KafkaAdmin kafkaAdmin;

    private SettlementBatchFixtureData fixtureData;

    @BeforeEach
    void setUp() {
        SettlementBatchTestFixture fixture = new SettlementBatchTestFixture(
                settlementJpaRepository,
                depositJpaRepository,
                adminMemberCode);
        fixtureData = fixture.create();
    }

    @AfterEach
    void tearDown() {
        jobRepositoryTestUtils.removeJobExecutions();
        depositHistoryJpaRepository.deleteAllInBatch();
        settlementJpaRepository.deleteAllInBatch();
        depositJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("정산 배치가 제대로 수행된다")
    void success_settlement_processes_target_settlements_and_deposits() throws Exception {
        // when
        JobExecution jobExecution = jobLauncher.run(settlementJob, jobParameters());

        // then
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus()); // 배치 실행 완료 확인

        long expectedSettledTotal = fixtureData.validBeforeSettlements().stream()
                .mapToLong(settlement -> expectedSettledAmount(settlement.getOriginalAmount()))
                .sum(); // 예상 전체 정산 금액 총 합

        for (SettlementEntity settlement : fixtureData.validBeforeSettlements()) {
            SettlementEntity updated = findSettlement(settlement);

            assertEquals(SettlementStatus.DONE, updated.getStatus());
            assertEquals(expectedSettledAmount(settlement.getOriginalAmount()), updated.getSettledAmount());
            assertEquals(0, settlementRate.compareTo(updated.getSettlementRate()));
            assertNotNull(updated.getSettledAt());
            assertNull(updated.getFailedAt());
        } // BEFORE -> DONE 정산 데이터 검증

        SettlementEntity invalidUpdated = findSettlement(fixtureData.invalidBeforeSettlement()); // FAILED 처리 데이터 검증
        assertEquals(SettlementStatus.FAILED, invalidUpdated.getStatus());
        assertNull(invalidUpdated.getSettledAmount());
        assertNull(invalidUpdated.getSettlementRate());
        assertNull(invalidUpdated.getSettledAt());
        assertNotNull(invalidUpdated.getFailedAt());

        // 변화 없는 데이터들 검증
        assertUnchanged(fixtureData.doneSettlement(), SettlementStatus.DONE);
        assertUnchanged(fixtureData.failedSettlement(), SettlementStatus.FAILED);
        assertUnchanged(fixtureData.outOfRangeSettlement(), SettlementStatus.BEFORE);
        assertUnchanged(fixtureData.endBoundarySettlement(), SettlementStatus.BEFORE);

        // 관리자 금액 변화 검증
        DepositEntity updatedAdminDeposit = findDeposit(adminMemberCode);
        assertEquals(ADMIN_INITIAL_AMOUNT - expectedSettledTotal, updatedAdminDeposit.getAmount());

        // 정산 받는 프리랜서 예치금 금액 변화 검증
        for (Map.Entry<String, DepositEntity> entry : fixtureData.receiverDeposits().entrySet()) {
            String receiverCode = entry.getKey();
            long receiverSettledTotal = fixtureData.validBeforeSettlements().stream()
                    .filter(settlement -> settlement.getReceiverCode().equals(receiverCode))
                    .mapToLong(settlement -> expectedSettledAmount(settlement.getOriginalAmount()))
                    .sum();

            assertEquals(RECEIVER_INITIAL_AMOUNT + receiverSettledTotal, findDeposit(receiverCode).getAmount());
        }

    }

    @Test
    @DisplayName("reader는 대상 기간의 BEFORE 정산만 읽는다.")
    void success_reader_reads_before_settlements_in_target_period() throws Exception {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters());

        List<SettlementEntity> readSettlements = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            List<SettlementEntity> items = new ArrayList<>();
            settlementZeroOffsetItemReader.open(new ExecutionContext());

            try {
                SettlementEntity item;
                while ((item = settlementZeroOffsetItemReader.read()) != null) {
                    items.add(item);
                    item.updateInfo(item.getSettledAmount(), item.getSettlementRate(), item.getSettledAt(),
                            SettlementStatus.DONE, item.getFailedAt());
                    settlementJpaRepository.saveAndFlush(item);
                }
            } finally {
                settlementZeroOffsetItemReader.close();
            }

            return items;
        });

        assertEquals(VALID_BEFORE_COUNT + 1, readSettlements.size());
        List<Long> readIds = readSettlements.stream()
                .map(SettlementEntity::getId)
                .toList();

        assertTrue(fixtureData.validBeforeSettlements().stream().map(SettlementEntity::getId).allMatch(readIds::contains));
        assertTrue(readIds.contains(fixtureData.invalidBeforeSettlement().getId()));
        assertFalse(readIds.contains(fixtureData.doneSettlement().getId()));
        assertFalse(readIds.contains(fixtureData.failedSettlement().getId()));
        assertFalse(readIds.contains(fixtureData.outOfRangeSettlement().getId()));
        assertFalse(readIds.contains(fixtureData.endBoundarySettlement().getId()));
        assertTrue(readSettlements.stream().allMatch(item ->
                !item.getProgressingAt().isBefore(TARGET_DATE.minusMonths(1))
                        && item.getProgressingAt().isBefore(TARGET_DATE)));
    }

    @Test
    @DisplayName("processor는 유효한 정산은 DONE으로, 유효하지 않은 정산은 FAILED로 변환한다.")
    void success_processor_settles_valid_settlement_and_fails_invalid_settlement() throws Exception {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters());

        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            Settlement valid = settlementDataProcessor.process(fixtureData.validBeforeSettlements().get(0));
            Settlement invalid = settlementDataProcessor.process(fixtureData.invalidBeforeSettlement());

            assertEquals(SettlementStatus.DONE, valid.getSettlementStatusInfo().status());
            assertEquals(expectedSettledAmount(fixtureData.validBeforeSettlements().get(0).getOriginalAmount()),
                    valid.getSettlementStatusInfo().settledAmount());
            assertEquals(0, settlementRate.compareTo(valid.getSettlementStatusInfo().settlementRate()));
            assertNotNull(valid.getSettlementTimeline().settledAt());
            assertNull(valid.getSettlementTimeline().failedAt());

            assertEquals(SettlementStatus.FAILED, invalid.getSettlementStatusInfo().status());
            assertNull(invalid.getSettlementStatusInfo().settledAmount());
            assertNull(invalid.getSettlementStatusInfo().settlementRate());
            assertNull(invalid.getSettlementTimeline().settledAt());
            assertNotNull(invalid.getSettlementTimeline().failedAt());

            return null;
        });
    }

    @Test
    @DisplayName("writer는 DONE 정산만 예치금을 이동하고 FAILED 정산은 예치금 처리를 하지 않는다.")
    void writer_updates_deposits_for_done_settlements_only() {
        SettlementEntity validEntity = fixtureData.validBeforeSettlements().get(0);
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters());

        new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
            try {
                StepScopeTestUtils.doInStepScope(stepExecution, () -> {
                    Settlement valid = settlementDataProcessor.process(validEntity);
                    Settlement invalid = settlementDataProcessor.process(fixtureData.invalidBeforeSettlement());

                    settlementCustomWriter.write(new Chunk<>(List.of(valid, invalid)));

                    return null;
                });
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });

        long settledAmount = expectedSettledAmount(validEntity.getOriginalAmount());

        assertEquals(ADMIN_INITIAL_AMOUNT - settledAmount, findDeposit(adminMemberCode).getAmount());
        assertEquals(RECEIVER_INITIAL_AMOUNT + settledAmount, findDeposit(validEntity.getReceiverCode()).getAmount());
        assertEquals(RECEIVER_INITIAL_AMOUNT, findDeposit(fixtureData.invalidBeforeSettlement().getReceiverCode()).getAmount());
        assertEquals(2L, depositHistoryJpaRepository.count());

        SettlementEntity validUpdated = findSettlement(validEntity);
        SettlementEntity invalidUpdated = findSettlement(fixtureData.invalidBeforeSettlement());

        assertEquals(SettlementStatus.DONE, validUpdated.getStatus());
        assertEquals(SettlementStatus.FAILED, invalidUpdated.getStatus());
        assertNotNull(validUpdated.getSettledAt());
        assertNotNull(invalidUpdated.getFailedAt());
    }

    private JobParameters jobParameters() {
        return new JobParametersBuilder()
                .addLocalDate("dateStr", TARGET_DATE)
                .toJobParameters();
    }

    private long expectedSettledAmount(Long originalAmount) {
        long fee = settlementRate.multiply(BigDecimal.valueOf(originalAmount)).longValue();
        return originalAmount - fee;
    }

    private SettlementEntity findSettlement(SettlementEntity settlement) {
        return settlementJpaRepository.findById(settlement.getId()).orElseThrow();
    }

    private DepositEntity findDeposit(String memberCode) {
        return depositJpaRepository.findByMemberCode(memberCode).orElseThrow();
    }

    private void assertUnchanged(SettlementEntity settlement, SettlementStatus expectedStatus) {
        SettlementEntity updated = findSettlement(settlement);

        assertEquals(expectedStatus, updated.getStatus());
        assertNull(updated.getSettledAmount());
        assertNull(updated.getSettlementRate());
        assertNull(updated.getSettledAt());
        assertNull(updated.getFailedAt());
    }
}
