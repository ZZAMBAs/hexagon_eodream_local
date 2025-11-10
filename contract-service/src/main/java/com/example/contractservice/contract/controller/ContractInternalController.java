package com.example.contractservice.contract.controller;

import com.example.contractservice.contract.common.swagger.annotation.GetContractInternalApi;
import com.example.contractservice.contract.controller.dto.response.ContractBriefResponse;
import com.example.contractservice.contract.service.ContractService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/contracts")
public class ContractInternalController {
    private final ContractService contractService;

    @GetContractInternalApi
    @GetMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    public ContractBriefResponse getBriefInfo(@PathVariable String code) {

        return new ContractBriefResponse("클라이언트 이름", "프리랜서 이름", Instant.now(), Instant.now(),
            "DONE", "계약명");
    }

}
