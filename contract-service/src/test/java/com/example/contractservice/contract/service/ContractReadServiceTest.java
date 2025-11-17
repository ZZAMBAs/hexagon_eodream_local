package com.example.contractservice.contract.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.contractservice.common.PaymentType;
import com.example.contractservice.contract.common.ContractStatus;
import com.example.contractservice.contract.common.Order;
import com.example.contractservice.contract.controller.dto.response.ContractBriefResponse;
import com.example.contractservice.contract.controller.dto.response.ContractListWithCursorResponse;
import com.example.contractservice.contract.entity.ContractEntity;
import com.example.contractservice.contract.repository.ContractJpaRepository;
import com.example.contractservice.contract.service.dto.request.ContractReadCursorRequest;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ContractReadServiceTest {

    private static final int PAGE_SIZE = 20;
    @Autowired
    private ContractJpaRepository contractJpaRepository;
    @Autowired
    private ContractReadService contractReadService;

    private String memberCode;

    @BeforeEach
    void setUp() {
        Random random = new Random(System.currentTimeMillis());
        memberCode = UUID.randomUUID().toString();

        for (int i = 0; i < 40; i++) {
            String opponentCode = UUID.randomUUID().toString();
            int zeroOrOne = random.nextInt(2);

            ContractEntity entity = ContractEntity.builder()
                    .code(UUID.randomUUID().toString())
                    .requestorCode(zeroOrOne % 2 == 0 ? memberCode : opponentCode)
                    .contractorCode(zeroOrOne % 2 == 0 ? opponentCode : memberCode)
                    .freelancerCode(opponentCode)
                    .name("이름" + i)
                    .body("내용" + i)
                    .status(ContractStatus.values()[random.nextInt(ContractStatus.values().length)]) // 상태 랜덤 선택
                    .startedAt(Instant.now())
                    .endedAt(Instant.now())
                    .unitAmount(random.nextLong(20000000L))
                    .paymentType(PaymentType.MONTHLY)
                    .build();

            contractJpaRepository.save(entity);
        } // 더미 데이터 40개
    }

    @AfterEach
    void tearDown() {
        contractJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 20개씩 자신의 계약을 내림차순으로 확인할 수 있다.")
    void success_find_all_contracts_given_normal_args() {
        // given - setUp()에서 수행
        // when
        ContractListWithCursorResponse firstResp = contractReadService.findAllBy(
                new ContractReadCursorRequest(memberCode, null, null, Order.DESC));// 최초 조회
        ContractListWithCursorResponse secondResp = contractReadService.findAllBy(
                new ContractReadCursorRequest(memberCode, firstResp.cursorDate(), firstResp.cursorCode(),
                        Order.DESC));// 두 번째 조회

        // then
        List<ContractBriefResponse> firstContracts = firstResp.contracts();
        List<ContractBriefResponse> secondContracts = secondResp.contracts();

        assertEquals(PAGE_SIZE, firstContracts.size());
        assertEquals(PAGE_SIZE, secondContracts.size());
        assertTrue(firstResp.hasNext());
        assertFalse(secondResp.hasNext());

        assertOrderDesc(firstContracts);
        assertOrderDesc(secondContracts);
    }

    private static void assertOrderDesc(List<ContractBriefResponse> contracts) {
        for (int i = 1; i < contracts.size(); i++) {
            Instant pre = contracts.get(i - 1).createdAt();
            Instant cur = contracts.get(i).createdAt();

            assertTrue(pre.isAfter(cur) || pre.equals(cur)); // 정렬 순서 검증
        }
    }
}
