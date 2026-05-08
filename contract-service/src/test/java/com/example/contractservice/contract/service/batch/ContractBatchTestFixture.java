package com.example.contractservice.contract.service.batch;

import com.example.contractservice.contract.common.ContractStatus;
import com.example.contractservice.contract.entity.ContractEntity;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hexagon.core.vo.PaymentType;

class ContractBatchTestFixture {

    static final Instant TARGET_MIDNIGHT = Instant.parse("2026-04-18T00:00:00Z");
    static final int TARGET_COUNT_PER_STATUS = 6;

    private ContractBatchTestFixture() {
    }

    static ContractBatchFixtureData create() {
        List<ContractEntity> inProgressExpiredContracts = createContracts(
                ContractStatus.IN_PROGRESS,
                TARGET_COUNT_PER_STATUS,
                TARGET_MIDNIGHT.minusSeconds(86_400),
                TARGET_MIDNIGHT.minusSeconds(1)
        );
        List<ContractEntity> paidDueContracts = createContracts(
                ContractStatus.PAID,
                TARGET_COUNT_PER_STATUS,
                TARGET_MIDNIGHT,
                TARGET_MIDNIGHT.plusSeconds(86_400)
        );
        List<ContractEntity> requestedDueContracts = createContracts(
                ContractStatus.REQUESTED,
                TARGET_COUNT_PER_STATUS,
                TARGET_MIDNIGHT.minusSeconds(1),
                TARGET_MIDNIGHT.plusSeconds(86_400)
        );

        return new ContractBatchFixtureData(
                inProgressExpiredContracts,
                paidDueContracts,
                requestedDueContracts,
                List.of(
                        createContract(ContractStatus.IN_PROGRESS, TARGET_MIDNIGHT.minusSeconds(86_400),
                                TARGET_MIDNIGHT),
                        createContract(ContractStatus.IN_PROGRESS, TARGET_MIDNIGHT.minusSeconds(86_400),
                                TARGET_MIDNIGHT.plusSeconds(1))
                ),
                List.of(
                        createContract(ContractStatus.PAID, TARGET_MIDNIGHT.plusSeconds(1),
                                TARGET_MIDNIGHT.plusSeconds(86_400)),
                        createContract(ContractStatus.PAID, TARGET_MIDNIGHT.plusSeconds(86_400),
                                TARGET_MIDNIGHT.plusSeconds(172_800))
                ),
                List.of(
                        createContract(ContractStatus.REQUESTED, TARGET_MIDNIGHT.plusSeconds(1),
                                TARGET_MIDNIGHT.plusSeconds(86_400)),
                        createContract(ContractStatus.REQUESTED, TARGET_MIDNIGHT.plusSeconds(86_400),
                                TARGET_MIDNIGHT.plusSeconds(172_800))
                ),
                createContract(ContractStatus.DONE, TARGET_MIDNIGHT.minusSeconds(86_400),
                        TARGET_MIDNIGHT.minusSeconds(1)),
                createContract(ContractStatus.CANCELLED, TARGET_MIDNIGHT.minusSeconds(86_400),
                        TARGET_MIDNIGHT.plusSeconds(86_400))
        );
    }

    private static List<ContractEntity> createContracts(ContractStatus status, int count, Instant startedAt,
            Instant endedAt) {
        List<ContractEntity> contracts = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            contracts.add(createContract(status, startedAt.minusSeconds(i), endedAt.minusSeconds(i)));
        }

        return contracts;
    }

    private static ContractEntity createContract(ContractStatus status, Instant startedAt, Instant endedAt) {
        return ContractEntity.builder()
                .clientCode(UUID.randomUUID().toString())
                .freelancerCode(UUID.randomUUID().toString())
                .code(UUID.randomUUID().toString())
                .commissionCode(UUID.randomUUID().toString())
                .startedAt(startedAt)
                .endedAt(endedAt)
                .paymentType(PaymentType.PER_JOB)
                .unitAmount(100_000L)
                .status(status)
                .name("계약 테스트")
                .body("계약 테스트 본문")
                .build();
    }
}

record ContractBatchFixtureData(
        List<ContractEntity> inProgressExpiredContracts,
        List<ContractEntity> paidDueContracts,
        List<ContractEntity> requestedDueContracts,
        List<ContractEntity> inProgressNotExpiredContracts,
        List<ContractEntity> paidNotStartedContracts,
        List<ContractEntity> requestedNotStartedContracts,
        ContractEntity doneContract,
        ContractEntity cancelledContract
) {
}
