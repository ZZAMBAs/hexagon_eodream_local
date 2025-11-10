package com.example.contractservice.settlement.service;

import com.example.contractservice.settlement.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettlementScheduledService {
    private SettlementRepository settlementRepository;

}
