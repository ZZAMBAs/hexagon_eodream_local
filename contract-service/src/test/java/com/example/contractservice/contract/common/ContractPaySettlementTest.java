package com.example.contractservice.contract.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.contractservice.common.TestConfig;
import com.example.contractservice.contract.entity.CommissionsCapacity;
import com.example.contractservice.contract.entity.ContractEntity;
import com.example.contractservice.contract.repository.CommissionsCapacityJpaRepository;
import com.example.contractservice.contract.repository.ContractJpaRepository;
import com.example.contractservice.contract.service.ContractPayService;
import com.example.contractservice.contract.service.dto.request.ContractPayProcessRequest;
import com.example.contractservice.deposit.entity.DepositEntity;
import com.example.contractservice.deposit.repository.DepositHistoryJpaRepository;
import com.example.contractservice.deposit.repository.DepositJpaRepository;
import com.example.contractservice.deposit.repository.DepositPendingJpaRepository;
import com.example.contractservice.settlement.common.SettlementStatus;
import com.example.contractservice.settlement.domain.Settlement;
import com.example.contractservice.settlement.entity.SettlementEntity;
import com.example.contractservice.settlement.repository.SettlementJpaRepository;
import com.example.contractservice.settlement.service.batch.writer.SettlementCustomWriter;
import com.example.contractservice.settlement.service.mapper.SettlementMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.hexagon.core.vo.PaymentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.Chunk;
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
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@SpringBatchTest
@Import(TestConfig.class)
@ActiveProfiles("batch-test")
class ContractPaySettlementTest {

    @Autowired
    private ContractPayService contractPayService;
    @Autowired
    private SettlementCustomWriter settlementCustomWriter;
    @Autowired
    private SettlementJpaRepository settlementJpaRepository;
    @Autowired
    private DepositJpaRepository depositJpaRepository;
    @Autowired
    private ContractJpaRepository contractJpaRepository;
    @Autowired
    private DepositHistoryJpaRepository depositHistoryJpaRepository;
    @Autowired
    private DepositPendingJpaRepository depositPendingJpaRepository;
    @Autowired
    private CommissionsCapacityJpaRepository commissionsCapacityJpaRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @MockitoBean
    private KafkaTemplate<String, String> kafkaTemplate;
    @MockitoBean
    private KafkaAdmin kafkaAdmin;

    @Value("${admin.member.code}")
    private String adminMemberCode;
    @Value("${batch.settlement.settlement-rate}")
    private BigDecimal settlementRate;

    @AfterEach
    void tearDown() {
        depositPendingJpaRepository.deleteAllInBatch();
        depositHistoryJpaRepository.deleteAllInBatch();
        settlementJpaRepository.deleteAllInBatch();
        contractJpaRepository.deleteAllInBatch();
        depositJpaRepository.deleteAllInBatch();
        commissionsCapacityJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("계약 결제와 정산 writer 예치금 처리가 동시에 일어나도 금액 정합성을 유지한다")
    void success_process_payment_and_settlement_writer_at_the_same_time() throws Exception {
        // given
        AtomicInteger paySuccessCount = new AtomicInteger();
        AtomicInteger settlementSuccessCount = new AtomicInteger();
        ConcurrentLinkedQueue<Throwable> errors = new ConcurrentLinkedQueue<>();

        long paymentAmount = 1_111L;
        long initialAmount = 500_000L;
        long settlementOriginalAmount = 10_000L;
        String userCode = UUID.randomUUID().toString();

        saveDeposit(userCode, initialAmount);
        saveDeposit(adminMemberCode, initialAmount);

        ContractPayProcessRequest payRequest = createPayRequest(userCode, paymentAmount);
        SettlementEntity settlementEntity = createSettlement(userCode, settlementOriginalAmount);
        Settlement settlement = SettlementMapper.toDomain(settlementEntity).settle(settlementRate);

        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        CyclicBarrier barrier = new CyclicBarrier(2);
        CountDownLatch countDownLatch = new CountDownLatch(2);
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();

        // when
        try {
            threadPool.execute(() -> runConcurrentTask(barrier, countDownLatch, errors, () -> {
                contractPayService.processPayment(payRequest);
                paySuccessCount.incrementAndGet();
            }));
            threadPool.execute(() -> runConcurrentTask(barrier, countDownLatch, errors, () -> {
                writeSettlement(stepExecution, settlement);
                settlementSuccessCount.incrementAndGet();
            }));

            assertTrue(countDownLatch.await(5, TimeUnit.SECONDS));
        } finally {
            threadPool.shutdownNow();
        }

        // then
        if (!errors.isEmpty()) {
            throw new AssertionError("동시 처리 실패", errors.peek());
        }

        long settlementFee = settlementRate.multiply(BigDecimal.valueOf(settlementOriginalAmount)).longValue();
        long settledAmount = settlementOriginalAmount - settlementFee;

        assertEquals(1, paySuccessCount.get());
        assertEquals(1, settlementSuccessCount.get());
        assertEquals(initialAmount - paymentAmount + settledAmount, findDeposit(userCode).getAmount());
        assertEquals(initialAmount - settledAmount, findDeposit(adminMemberCode).getAmount());
        assertEquals(SettlementStatus.DONE, settlementJpaRepository.findById(settlementEntity.getId()).orElseThrow().getStatus());
    }

    private void writeSettlement(StepExecution stepExecution, Settlement settlement) {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                StepScopeTestUtils.doInStepScope(stepExecution, () -> {
                    settlementCustomWriter.write(new Chunk<>(List.of(settlement)));
                    return null;
                });
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private void runConcurrentTask(CyclicBarrier barrier, CountDownLatch countDownLatch,
            ConcurrentLinkedQueue<Throwable> errors, Runnable task) {
        try {
            barrier.await();
            task.run();
        } catch (Throwable e) {
            errors.add(e);
        } finally {
            countDownLatch.countDown();
        }
    }

    private ContractPayProcessRequest createPayRequest(String userCode, long paymentAmount) {
        ContractEntity contract = contractJpaRepository.save(ContractEntity.builder()
                .clientCode(userCode)
                .freelancerCode(UUID.randomUUID().toString())
                .code(UUID.randomUUID().toString())
                .commissionCode(UUID.randomUUID().toString())
                .startedAt(Instant.now().plus(1L, ChronoUnit.DAYS))
                .endedAt(Instant.now().plus(3L, ChronoUnit.DAYS))
                .unitAmount(paymentAmount)
                .paymentType(PaymentType.PER_JOB)
                .status(ContractStatus.REQUESTED)
                .name("name")
                .body("body")
                .build());

        commissionsCapacityJpaRepository.save(CommissionsCapacity.createBy(contract.getCommissionCode(), 50, 30));

        return new ContractPayProcessRequest(userCode, contract.getCode());
    }

    private SettlementEntity createSettlement(String receiverCode, long originalAmount) {
        return settlementJpaRepository.save(SettlementEntity.builder()
                .contractCode(UUID.randomUUID().toString())
                .receiverCode(receiverCode)
                .originalAmount(originalAmount)
                .status(SettlementStatus.BEFORE)
                .progressingAt(LocalDate.now().minusDays(1L))
                .createdAt(Instant.now())
                .code(UUID.randomUUID().toString())
                .build());
    }

    private void saveDeposit(String memberCode, long amount) {
        DepositEntity deposit = DepositEntity.createBy(memberCode);
        deposit.updateInfo(amount);
        depositJpaRepository.save(deposit);
    }

    private DepositEntity findDeposit(String memberCode) {
        return depositJpaRepository.findByMemberCode(memberCode).orElseThrow();
    }
}
