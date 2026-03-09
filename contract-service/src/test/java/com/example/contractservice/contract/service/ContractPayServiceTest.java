package com.example.contractservice.contract.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.contractservice.contract.common.ContractStatus;
import com.example.contractservice.contract.domain.Contract;
import com.example.contractservice.contract.domain.vo.ContractContent;
import com.example.contractservice.contract.domain.vo.ContractInfo;
import com.example.contractservice.contract.entity.CommissionsCapacity;
import com.example.contractservice.contract.repository.CommissionsCapacityRepository;
import com.example.contractservice.contract.repository.ContractRepository;
import com.example.contractservice.contract.service.dto.request.ContractPayProcessRequest;
import com.example.contractservice.deposit.service.DepositPendingService;
import com.example.contractservice.deposit.service.DepositService;
import com.example.contractservice.deposit.service.dto.request.DepositPendingSaveRequest;
import com.example.contractservice.deposit.service.dto.request.DepositProcessRequest;
import com.example.contractservice.settlement.service.SettlementService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.hexagon.core.vo.PaymentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class ContractPayServiceTest {

    @Mock
    private DepositService depositService;
    @Mock
    private DepositPendingService depositPendingService;
    @Mock
    private SettlementService settlementService;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private CommissionsCapacityRepository commissionsCapacityRepository;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private ContractPayService contractPayService;

    @Test
    @DisplayName("결제 처리에 성공하면 사용자 출금 후 관리자 입금 pending을 저장한다")
    void success_process_payment_saves_pending_instead_of_admin_deposit_update() {
        // given
        String clientCode = UUID.randomUUID().toString();
        String freelancerCode = UUID.randomUUID().toString();
        String contractCode = UUID.randomUUID().toString();
        String commissionCode = UUID.randomUUID().toString();
        long unitAmount = 150_000L;

        Contract contract = new Contract(
                contractCode,
                new ContractInfo(
                        clientCode,
                        freelancerCode,
                        commissionCode,
                        Instant.now().plus(1, ChronoUnit.DAYS),
                        Instant.now().plus(31, ChronoUnit.DAYS),
                        PaymentType.PER_JOB,
                        unitAmount,
                        ContractStatus.REQUESTED
                ),
                new ContractContent("name", "body"),
                Instant.now(),
                Instant.now()
        );
        CommissionsCapacity capacity = CommissionsCapacity.createBy(commissionCode, 10, 5);

        when(contractRepository.findByCode(contractCode)).thenReturn(contract);
        when(commissionsCapacityRepository.findByCommissionCode(commissionCode)).thenReturn(capacity);

        // when
        contractPayService.processPayment(new ContractPayProcessRequest(clientCode, contractCode));

        // then
        ArgumentCaptor<DepositProcessRequest> withdrawCaptor = ArgumentCaptor.forClass(DepositProcessRequest.class);
        ArgumentCaptor<DepositPendingSaveRequest> pendingCaptor = ArgumentCaptor.forClass(DepositPendingSaveRequest.class);

        verify(depositService).withdraw(withdrawCaptor.capture());
        verify(depositPendingService).save(pendingCaptor.capture());
        verify(contractRepository).saveContract(any(Contract.class));
        verify(settlementService).savePaidSettlements(any());
        verify(commissionsCapacityRepository).saveCapacity(capacity);
        verify(applicationEventPublisher).publishEvent(any(Object.class));

        assertEquals(clientCode, withdrawCaptor.getValue().memberCode());
        assertEquals(contractCode, withdrawCaptor.getValue().contractCode());
        assertEquals(unitAmount, withdrawCaptor.getValue().amount());
        assertEquals(unitAmount, pendingCaptor.getValue().amount());
        assertEquals(contractCode, pendingCaptor.getValue().contractCode());
        assertEquals(ContractStatus.PAID, contract.getInfo().status());
        assertEquals(1, capacity.getSelectedCount());
    }
}
