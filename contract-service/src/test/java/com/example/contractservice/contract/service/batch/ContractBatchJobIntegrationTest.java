package com.example.contractservice.contract.service.batch;

import static com.example.contractservice.contract.service.batch.ContractBatchTestFixture.TARGET_COUNT_PER_STATUS;
import static com.example.contractservice.contract.service.batch.ContractBatchTestFixture.TARGET_MIDNIGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.contractservice.common.TestConfig;
import com.example.contractservice.contract.common.ContractStatus;
import com.example.contractservice.contract.entity.ContractEntity;
import com.example.contractservice.contract.repository.ContractJpaRepository;
import com.example.contractservice.contract.service.batch.reader.ContractInProgressReader;
import com.example.contractservice.contract.service.batch.reader.ContractPaidReader;
import com.example.contractservice.contract.service.batch.reader.ContractRequestedReader;
import com.example.contractservice.contract.service.batch.writer.ContractCancelledWriter;
import com.example.contractservice.contract.service.batch.writer.ContractDoneWriter;
import com.example.contractservice.contract.service.batch.writer.ContractInProgressWriter;
import com.example.contractservice.contract.service.mapper.ContractMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@SpringBatchTest
@Import(TestConfig.class)
@ActiveProfiles("batch-test")
class ContractBatchJobIntegrationTest {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job statusChangeJob;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private ContractJpaRepository contractJpaRepository;
    @Autowired
    private ContractInProgressReader contractInProgressReader;
    @Autowired
    private ContractPaidReader contractPaidReader;
    @Autowired
    private ContractRequestedReader contractRequestedReader;
    @Autowired
    private ContractDoneWriter contractDoneWriter;
    @Autowired
    private ContractInProgressWriter contractInProgressWriter;
    @Autowired
    private ContractCancelledWriter contractCancelledWriter;

    @MockitoBean
    private KafkaTemplate<String, String> kafkaTemplate;
    @MockitoBean
    private KafkaAdmin kafkaAdmin;

    private ContractBatchFixtureData fixtureData;

    @BeforeEach
    void setUp() {
        fixtureData = saveFixtureData(ContractBatchTestFixture.create());
    }

    @AfterEach
    void tearDown() {
        jobRepositoryTestUtils.removeJobExecutions();
        contractJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("계약 상태 변경 배치가 대상 계약만 상태 전이한다")
    void success_contract_batch_changes_only_due_contract_statuses() throws Exception {
        // when
        JobExecution jobExecution = jobLauncher.run(statusChangeJob, jobParameters());

        // then
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        assertStepCounts(jobExecution);

        assertStatuses(fixtureData.inProgressExpiredContracts(), ContractStatus.DONE);
        assertStatuses(fixtureData.paidDueContracts(), ContractStatus.IN_PROGRESS);
        assertStatuses(fixtureData.requestedDueContracts(), ContractStatus.CANCELLED);

        assertStatuses(fixtureData.inProgressNotExpiredContracts(), ContractStatus.IN_PROGRESS);
        assertStatuses(fixtureData.paidNotStartedContracts(), ContractStatus.PAID);
        assertStatuses(fixtureData.requestedNotStartedContracts(), ContractStatus.REQUESTED);
        assertStatus(fixtureData.doneContract(), ContractStatus.DONE);
        assertStatus(fixtureData.cancelledContract(), ContractStatus.CANCELLED);
    }

    @Test
    @DisplayName("reader는 각 상태와 기준 일시에 맞는 계약만 읽는다")
    void success_readers_read_only_due_contracts_for_each_status() throws Exception {
        // when
        List<ContractEntity> inProgressRead = readAll(contractInProgressReader);
        List<ContractEntity> paidRead = readAll(contractPaidReader);
        List<ContractEntity> requestedRead = readAll(contractRequestedReader);

        // then
        assertReadContracts(inProgressRead, fixtureData.inProgressExpiredContracts(),
                List.of(fixtureData.inProgressNotExpiredContracts(), fixtureData.paidDueContracts(),
                        fixtureData.requestedDueContracts()));
        assertReadContracts(paidRead, fixtureData.paidDueContracts(),
                List.of(fixtureData.paidNotStartedContracts(), fixtureData.inProgressExpiredContracts(),
                        fixtureData.requestedDueContracts()));
        assertReadContracts(requestedRead, fixtureData.requestedDueContracts(),
                List.of(fixtureData.requestedNotStartedContracts(), fixtureData.inProgressExpiredContracts(),
                        fixtureData.paidDueContracts()));
    }

    @Test
    @DisplayName("writer는 계약 상태를 각각 DONE, IN_PROGRESS, CANCELLED로 변경한다")
    void success_writers_change_contract_statuses() throws Exception {
        // given
        ContractEntity inProgress = fixtureData.inProgressExpiredContracts().get(0);
        ContractEntity paid = fixtureData.paidDueContracts().get(0);
        ContractEntity requested = fixtureData.requestedDueContracts().get(0);
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters());

        // when
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            contractDoneWriter.write(new Chunk<>(List.of(ContractMapper.toDomain(inProgress))));
            contractInProgressWriter.write(new Chunk<>(List.of(ContractMapper.toDomain(paid))));
            contractCancelledWriter.write(new Chunk<>(List.of(ContractMapper.toDomain(requested))));

            return null;
        });

        // then
        assertStatus(inProgress, ContractStatus.DONE);
        assertStatus(paid, ContractStatus.IN_PROGRESS);
        assertStatus(requested, ContractStatus.CANCELLED);

        assertStatuses(fixtureData.inProgressExpiredContracts().subList(1, fixtureData.inProgressExpiredContracts().size()),
                ContractStatus.IN_PROGRESS);
        assertStatuses(fixtureData.paidDueContracts().subList(1, fixtureData.paidDueContracts().size()),
                ContractStatus.PAID);
        assertStatuses(fixtureData.requestedDueContracts().subList(1, fixtureData.requestedDueContracts().size()),
                ContractStatus.REQUESTED);
    }

    private JobParameters jobParameters() {
        return new JobParametersBuilder()
                .addString("dateStr", TARGET_MIDNIGHT.toString())
                .toJobParameters();
    }

    private ContractBatchFixtureData saveFixtureData(ContractBatchFixtureData fixtureData) {
        return new ContractBatchFixtureData(
                saveAll(fixtureData.inProgressExpiredContracts()),
                saveAll(fixtureData.paidDueContracts()),
                saveAll(fixtureData.requestedDueContracts()),
                saveAll(fixtureData.inProgressNotExpiredContracts()),
                saveAll(fixtureData.paidNotStartedContracts()),
                saveAll(fixtureData.requestedNotStartedContracts()),
                save(fixtureData.doneContract()),
                save(fixtureData.cancelledContract())
        );
    }

    private List<ContractEntity> saveAll(List<ContractEntity> contracts) {
        return contractJpaRepository.saveAll(contracts);
    }

    private ContractEntity save(ContractEntity contract) {
        return contractJpaRepository.save(contract);
    }

    private void assertStepCounts(JobExecution jobExecution) {
        Map<String, StepExecution> stepExecutions = jobExecution.getStepExecutions().stream()
                .collect(Collectors.toMap(StepExecution::getStepName, Function.identity()));

        assertEquals(3, stepExecutions.size());
        assertStepCount(stepExecutions.get("contractToDoneBatchStep"));
        assertStepCount(stepExecutions.get("contractToInProgressBatchStep"));
        assertStepCount(stepExecutions.get("contractToCancelledBatchStep"));
    }

    private void assertStepCount(StepExecution stepExecution) {
        assertEquals(BatchStatus.COMPLETED, stepExecution.getStatus());
        assertEquals(TARGET_COUNT_PER_STATUS, stepExecution.getReadCount());
        assertEquals(TARGET_COUNT_PER_STATUS, stepExecution.getWriteCount());
        assertEquals(0, stepExecution.getRollbackCount());
    }

    private List<ContractEntity> readAll(ItemStreamReader<ContractEntity> reader) throws Exception {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters());

        return StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            List<ContractEntity> items = new ArrayList<>();
            reader.open(new ExecutionContext());

            try {
                ContractEntity item;
                while ((item = reader.read()) != null) {
                    items.add(item);
                }
            } finally {
                reader.close();
            }

            return items;
        });
    }

    private void assertReadContracts(List<ContractEntity> actual, List<ContractEntity> expected,
            List<List<ContractEntity>> unexpectedGroups) {
        List<Long> actualIds = actual.stream()
                .map(ContractEntity::getId)
                .toList();

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.stream().map(ContractEntity::getId).allMatch(actualIds::contains));
        assertFalse(unexpectedGroups.stream()
                .flatMap(Collection::stream)
                .map(ContractEntity::getId)
                .anyMatch(actualIds::contains));
    }

    private void assertStatuses(List<ContractEntity> contracts, ContractStatus expectedStatus) {
        contracts.forEach(contract -> assertStatus(contract, expectedStatus));
    }

    private void assertStatus(ContractEntity contract, ContractStatus expectedStatus) {
        ContractEntity updated = contractJpaRepository.findById(contract.getId()).orElseThrow();

        assertEquals(expectedStatus, updated.getStatus());
    }
}
