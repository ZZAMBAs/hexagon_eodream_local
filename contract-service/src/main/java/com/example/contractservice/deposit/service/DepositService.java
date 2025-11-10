package com.example.contractservice.deposit.service;

import com.example.contractservice.deposit.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepositService {
    private final DepositRepository depositRepository;

}
