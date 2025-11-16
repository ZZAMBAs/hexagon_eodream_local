package com.example.contractservice.deposit.service;

import static com.example.contractservice.deposit.service.mapper.DepositHistoryMapper.toEntity;
import static com.example.contractservice.deposit.service.mapper.DepositMapper.toDomain;

import com.example.contractservice.deposit.domain.Deposit;
import com.example.contractservice.deposit.domain.DepositHistory;
import com.example.contractservice.deposit.entity.DepositEntity;
import com.example.contractservice.deposit.entity.DepositHistoryEntity;
import com.example.contractservice.deposit.repository.DepositRepository;
import com.example.contractservice.deposit.service.dto.request.DepositProcessRequest;
import com.example.contractservice.deposit.service.dto.response.DepositWithdrawResponse;
import com.example.contractservice.deposit.service.mapper.DepositHistoryMapper;
import com.example.contractservice.deposit.service.mapper.DepositMapper;
import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepositService {
    private final DepositRepository depositRepository;

    /**
     * 공통적인 메서드입니다. action은 예치금에 어떤 행동(출금/입금)을 하는지가 들어갑니다. </br>
     * 다음 순서로 처리됩니다. </br></br>
     * 1. 예치금을 DB에서 가져온다. </br>
     * 2. 예치금에 action(출금/입금 등) 한다. </br>
     * 3. 변동된 예치금을 바탕으로 예치금 내역을 만들고 저장한다. </br>
     */
    @Transactional
    public DepositWithdrawResponse process(DepositProcessRequest request, BiConsumer<Deposit, Long> action) {
        DepositEntity depositEntity = depositRepository.findDepositByMemberCode(request.memberCode());
        Deposit deposit = toDomain(depositEntity);

        Long beforeAmount = deposit.getAmount();
        action.accept(deposit, request.amount());
        Long afterAmount = deposit.getAmount();

        DepositMapper.applyToEntity(deposit, depositEntity);
        depositRepository.saveDeposit(depositEntity);

        saveHistory(request, deposit, afterAmount - beforeAmount);

        return new DepositWithdrawResponse(depositEntity.getCode(), depositEntity.getMemberCode(),
                depositEntity.getAmount());
    }

    private void saveHistory(DepositProcessRequest request, Deposit deposit, Long changeAmount) {
        DepositHistory depositHistory = DepositHistoryMapper.toDomain(deposit, changeAmount, request.summary());
        DepositHistoryEntity depositHistoryEntity = toEntity(depositHistory);

        depositRepository.saveDepositHistory(depositHistoryEntity);
    }

    /** 출금 action
     */
    public void withdraw(Deposit deposit, Long amount) {
        deposit.withdraw(amount);
    }

    /** 입금 action
     */
    public void transfer(Deposit deposit, Long amount) {
        deposit.transfer(amount);
    }
}
