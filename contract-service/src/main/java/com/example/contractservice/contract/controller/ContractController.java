package com.example.contractservice.contract.controller;

import com.example.contractservice.contract.common.swagger.annotation.ContractCancelApi;
import com.example.contractservice.contract.common.swagger.annotation.ContractConfirmApi;
import com.example.contractservice.contract.common.swagger.annotation.ContractCreateApi;
import com.example.contractservice.contract.common.swagger.annotation.ContractPayApi;
import com.example.contractservice.contract.common.swagger.annotation.GetContractByCodeApi;
import com.example.contractservice.contract.common.swagger.annotation.GetContractsApi;
import com.example.contractservice.contract.controller.dto.request.ContractCreateRequest;
import com.example.contractservice.contract.controller.dto.response.ContractBriefResponse;
import com.example.contractservice.contract.controller.dto.response.ContractCreateResponse;
import com.example.contractservice.contract.controller.dto.response.ContractDetailResponse;
import com.example.contractservice.contract.controller.dto.response.ContractInfoResponse;
import com.example.contractservice.contract.service.ContractService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;

    @GetContractsApi
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ContractBriefResponse> getContracts(@RequestHeader(name = "X-CODE") String xCode,
        @RequestParam(value = "cursor-date", required = false) String cursorDate,
        @RequestParam(value = "cursor-code", required = false) String cursorCode) {

        return List.of(
            new ContractBriefResponse("클라이언트 이름1", "프리랜서 이름1", Instant.now(), Instant.now(), "PAID",
                "계약명1"),
            new ContractBriefResponse("클라이언트 이름2", "프리랜서 이름2", Instant.now(), Instant.now(),
                "IN_PROGRESS", "계약명2"));
    }

    @GetContractByCodeApi
    @GetMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    public ContractDetailResponse getContractByCode(@RequestHeader(name = "X-CODE") String xCode,
        @PathVariable String code) {

        return new ContractDetailResponse("클라이언트 이름", "프리랜서 이름", Instant.now(), Instant.now(),
            Instant.now(), "ONE_TIME", "REQUESTED", "계약명", "계약 내용");
    }

    @ContractCreateApi
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContractCreateResponse createContract(@RequestBody ContractCreateRequest request) {

        return new ContractCreateResponse(UUID.randomUUID().toString());
    }

    @ContractConfirmApi
    @PostMapping("/{code}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public ContractInfoResponse confirmContract(@RequestHeader(name = "X-CODE") String xCode,
        @PathVariable String code) {

        return new ContractInfoResponse(UUID.randomUUID().toString(), "CONFIRMED");
    }

    @ContractPayApi
    @PostMapping("/{code}/pay")
    @ResponseStatus(HttpStatus.OK)
    public ContractInfoResponse payContract(@RequestHeader(name = "X-CODE") String xCode,
        @PathVariable String code) {

        return new ContractInfoResponse(UUID.randomUUID().toString(), "PAID");
    }

    @ContractCancelApi
    @PostMapping("/{code}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ContractInfoResponse cancelContract(@RequestHeader(name = "X-CODE") String xCode,
        @PathVariable String code) {

        return new ContractInfoResponse(UUID.randomUUID().toString(), "CANCELLED");
    }
}
