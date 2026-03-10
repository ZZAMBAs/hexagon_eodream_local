package com.example.contractservice.contract.service;

import static com.example.contractservice.contract.domain.exception.ContractErrorCode.CANCEL_NOT_AVAILABLE;

import com.example.contractservice.contract.controller.dto.response.CommissionCapacityResponse;
import com.example.contractservice.contract.domain.Contract;
import com.example.contractservice.contract.domain.exception.ContractException;
import com.example.contractservice.contract.repository.ContractRepository;
import com.example.contractservice.contract.service.mapper.ContractMapper;
import com.example.contractservice.deposit.controller.dto.response.DepositHistoryInfo;
import com.example.contractservice.deposit.service.DepositPendingService;
import com.example.contractservice.deposit.service.DepositService;
import com.example.contractservice.deposit.service.dto.request.DepositProcessRequest;
import com.example.contractservice.settlement.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.hexagon.core.events.contract.CommissionOpenCloseEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContractCancelService {

    private static final String REFUND_COMMENT = "환불 처리";

    private final DepositService depositService;
    private final DepositPendingService depositPendingService;
    private final SettlementService settlementService;
    private final ContractRepository contractRepository;
    private final CommissionsCapacityService commissionsCapacityService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void processCancel(Contract contract) {

        if (!(contract.isRequested() || contract.isPaid())) {
            throw new ContractException(CANCEL_NOT_AVAILABLE);
        }

        if (contract.isPaid()) {
            rollbackPaidContract(contract);
        }

        contract.cancel();

        contractRepository.saveContract(contract);

        applicationEventPublisher.publishEvent(ContractMapper.toContractEvent(contract));
    }

    private void rollbackPaidContract(Contract contract) {
        cancelPending(contract);
        publishEventIfCommissionFull(contract);
        refund(contract);
        deleteCancelableSettlements(contract);
    }

    private void cancelPending(Contract contract) {
        boolean isCancelled = depositPendingService.cancelPendingByContractCode(contract.getCode());

        if (!isCancelled) {
            throw new ContractException(CANCEL_NOT_AVAILABLE);
        }
    }

    private void publishEventIfCommissionFull(Contract contract) {
        String commissionCode = contract.getInfo().commissionCode();
        CommissionCapacityResponse capacityResponse = commissionsCapacityService.getCapacity(commissionCode);

        if (capacityResponse.selectionCapacity() == capacityResponse.selectedCapacity()) { // 의뢰글 마감 상황이었을 경우
            applicationEventPublisher.publishEvent(new CommissionOpenCloseEvent(commissionCode, true));
        }
    }

    /** 결제한 유저 예치금으로 transfer
     *
     * @param contract 환불을 진행할 계약
     */
    private void refund(Contract contract) {
        DepositHistoryInfo historyInfo = depositService.getDepositHistoryForRefund(
                contract.getInfo().clientCode(), contract.getCode());
        DepositProcessRequest memberTransferRequest = new DepositProcessRequest(
                contract.getInfo().clientCode(),
                contract.getCode(),
                Math.abs(historyInfo.changeAmount()),
                REFUND_COMMENT
        );

        depositService.transfer(memberTransferRequest);
    }

    private void deleteCancelableSettlements(Contract contract) {
        settlementService.deleteCancelableSettlements(contract);
    }
}
