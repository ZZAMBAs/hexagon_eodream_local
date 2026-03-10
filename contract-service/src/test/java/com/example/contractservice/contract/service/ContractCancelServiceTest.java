package com.example.contractservice.contract.service;

import static com.example.contractservice.contract.domain.exception.ContractErrorCode.CANCEL_NOT_AVAILABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.contractservice.contract.common.ContractStatus;
import com.example.contractservice.contract.controller.dto.response.CommissionCapacityResponse;
import com.example.contractservice.contract.domain.Contract;
import com.example.contractservice.contract.domain.exception.ContractException;
import com.example.contractservice.contract.domain.vo.ContractContent;
import com.example.contractservice.contract.domain.vo.ContractInfo;
import com.example.contractservice.contract.repository.ContractRepository;
import com.example.contractservice.deposit.controller.dto.response.DepositHistoryInfo;
import com.example.contractservice.deposit.service.DepositPendingService;
import com.example.contractservice.deposit.service.DepositService;
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
class ContractCancelServiceTest {

    @Mock
    private DepositService depositService;
    @Mock
    private DepositPendingService depositPendingService;
    @Mock
    private SettlementService settlementService;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private CommissionsCapacityService commissionsCapacityService;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private ContractCancelService contractCancelService;

    @Test
    @DisplayName("PAID 怨꾩빟 痍⑥냼???깃났?섎㈃ deposit pending??CANCELLED濡?泥섎━?섍퀬 ?뚯썝 ?덉튂湲덉쓣 ?섎텋?쒕떎")
    void success_process_cancel_paid_contract_when_pending_is_pending() {
        // given
        String clientCode = UUID.randomUUID().toString();
        String freelancerCode = UUID.randomUUID().toString();
        String contractCode = UUID.randomUUID().toString();
        String commissionCode = UUID.randomUUID().toString();
        long amount = 300_000L;

        Contract contract = new Contract(
                contractCode,
                new ContractInfo(
                        clientCode,
                        freelancerCode,
                        commissionCode,
                        Instant.now().plus(1, ChronoUnit.DAYS),
                        Instant.now().plus(31, ChronoUnit.DAYS),
                        PaymentType.PER_JOB,
                        amount,
                        ContractStatus.PAID
                ),
                new ContractContent("name", "body"),
                Instant.now(),
                Instant.now()
        );

        when(depositPendingService.cancelPendingByContractCode(contractCode)).thenReturn(true);
        when(commissionsCapacityService.getCapacity(commissionCode))
                .thenReturn(new CommissionCapacityResponse(10, 5, 5, 3));
        when(depositService.getDepositHistoryForRefund(clientCode, contractCode))
                .thenReturn(new DepositHistoryInfo(Instant.now(), -amount, 700_000L, "payment"));

        // when
        contractCancelService.processCancel(contract);

        // then
        ArgumentCaptor<DepositProcessRequest> refundCaptor = ArgumentCaptor.forClass(DepositProcessRequest.class);

        verify(depositPendingService).cancelPendingByContractCode(contractCode);
        verify(depositService).transfer(refundCaptor.capture());
        verify(depositService, never()).withdraw(any());
        verify(settlementService).deleteCancelableSettlements(contract);
        verify(contractRepository).saveContract(contract);
        verify(applicationEventPublisher).publishEvent(any(Object.class));

        assertEquals(clientCode, refundCaptor.getValue().memberCode());
        assertEquals(contractCode, refundCaptor.getValue().contractCode());
        assertEquals(amount, refundCaptor.getValue().amount());
        assertEquals(ContractStatus.CANCELLED, contract.getInfo().status());
    }

    @Test
    @DisplayName("PAID 怨꾩빟??deposit pending???대? COMPLETED硫?痍⑥냼???ㅽ뙣?쒕떎")
    void fail_process_cancel_paid_contract_when_pending_is_completed() {
        // given
        String clientCode = UUID.randomUUID().toString();
        String freelancerCode = UUID.randomUUID().toString();
        String contractCode = UUID.randomUUID().toString();
        String commissionCode = UUID.randomUUID().toString();

        Contract contract = new Contract(
                contractCode,
                new ContractInfo(
                        clientCode,
                        freelancerCode,
                        commissionCode,
                        Instant.now().plus(1, ChronoUnit.DAYS),
                        Instant.now().plus(31, ChronoUnit.DAYS),
                        PaymentType.PER_JOB,
                        300_000L,
                        ContractStatus.PAID
                ),
                new ContractContent("name", "body"),
                Instant.now(),
                Instant.now()
        );

        when(depositPendingService.cancelPendingByContractCode(contractCode)).thenReturn(false);

        // when & then
        ContractException exception = assertThrows(ContractException.class,
                () -> contractCancelService.processCancel(contract));

        assertEquals(CANCEL_NOT_AVAILABLE, exception.getErrorCode());
        verify(depositPendingService).cancelPendingByContractCode(contractCode);
        verify(depositService, never()).getDepositHistoryForRefund(any(), any());
        verify(depositService, never()).transfer(any());
        verify(settlementService, never()).deleteCancelableSettlements(any());
        verify(contractRepository, never()).saveContract(any());
    }
}
