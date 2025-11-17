package com.example.contractservice.contract.controller;

import com.example.contractservice.common.ResponseDto;
import com.example.contractservice.contract.common.swagger.annotation.ContractPayApi;
import com.example.contractservice.contract.common.swagger.annotation.GetContractInternalApi;
import com.example.contractservice.contract.controller.dto.request.ContractPayRequest;
import com.example.contractservice.contract.controller.dto.response.ContractBriefResponse;
import com.example.contractservice.contract.controller.dto.response.ContractInfoResponse;
import com.example.contractservice.contract.service.ContractService;
import com.example.contractservice.contract.service.dto.request.ContractPayProcessRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/contracts")
public class ContractInternalController {
    private final ContractService contractService;

    @ContractPayApi
    @PostMapping("/pay")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<List<ContractInfoResponse>> payContract(@RequestHeader(name = "X-CODE") String xCode,
            @RequestBody ContractPayRequest request) {

        ContractPayProcessRequest serviceRequest = new ContractPayProcessRequest(xCode, request.codes());

        return ResponseDto.ok(contractService.payContracts(serviceRequest));
    }

    @GetContractInternalApi
    @GetMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    public ContractBriefResponse getBriefInfo(@PathVariable String code) {

        throw new UnsupportedOperationException(); // 임시 조치
    }

}
