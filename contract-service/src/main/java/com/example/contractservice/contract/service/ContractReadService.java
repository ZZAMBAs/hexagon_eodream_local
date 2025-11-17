package com.example.contractservice.contract.service;

import com.example.contractservice.contract.controller.dto.response.ContractListWithCursorResponse;
import com.example.contractservice.contract.entity.ContractEntity;
import com.example.contractservice.contract.repository.ContractRepository;
import com.example.contractservice.contract.service.dto.request.ContractReadCursorRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractReadService {

    private static final int PAGE_SIZE = 20;

    private final ContractRepository contractRepository;

    public ContractListWithCursorResponse findAllBy(ContractReadCursorRequest request) {
        List<ContractEntity> contractEntities = contractRepository.findAllBy(request.memberCode(), request.cursor(),
                request.cursorCode(), request.order(), PAGE_SIZE);

        return ContractListWithCursorResponse.of(contractEntities, PAGE_SIZE);
    }
}
