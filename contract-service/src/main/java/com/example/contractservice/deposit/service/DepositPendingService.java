package com.example.contractservice.deposit.service;

import com.example.contractservice.deposit.repository.DepositPendingRepository;
import com.example.contractservice.deposit.service.dto.request.DepositPendingSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepositPendingService {
    private final DepositPendingRepository depositPendingRepository;

    public void save(DepositPendingSaveRequest depositPendingSaveRequest) {
        depositPendingRepository.save(depositPendingSaveRequest.toDomain());
    }

    public boolean cancelPendingByContractCode(String contractCode) {
        return depositPendingRepository.cancelPendingByContractCode(contractCode);
    }
}
