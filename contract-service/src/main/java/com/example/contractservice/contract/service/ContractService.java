package com.example.contractservice.contract.service;

import static com.example.contractservice.contract.domain.exception.ContractErrorCode.*;
import static com.example.contractservice.contract.service.mapper.ContractMapper.*;

import com.example.contractservice.contract.common.ContractStatus;
import com.example.contractservice.contract.controller.dto.request.ContractCreateRequest;
import com.example.contractservice.contract.controller.dto.response.ContractCreateResponse;
import com.example.contractservice.contract.controller.dto.response.ContractInfoResponse;
import com.example.contractservice.contract.domain.Contract;
import com.example.contractservice.contract.domain.exception.ContractException;
import com.example.contractservice.contract.domain.vo.ContractInfo;
import com.example.contractservice.contract.entity.ContractEntity;
import com.example.contractservice.contract.event.dto.ContractConfirmEvent;
import com.example.contractservice.contract.repository.ContractRepository;
import com.example.contractservice.contract.service.dto.request.ContractConfirmRequest;
import com.example.contractservice.contract.service.dto.response.MemberInfoResponse;
import com.example.contractservice.contract.service.dto.response.MemberInfoResponse.MemberInfo;
import com.example.contractservice.contract.service.mapper.ContractMapper;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;
    private final RestTemplate restTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${module.member.application.name}")
    private String memberServiceName;

    @Value("${module.member.application.path.member-info}")
    private String memberInfoUrl;

    @Transactional
    public ContractCreateResponse requestContract(ContractCreateRequest request) {
        isValidMember(List.of(request.requestorCode(), request.contractorCode()));

        Contract createdContract = request.toContract();

        ContractEntity contractEntity = contractRepository.saveContract(toEntity(createdContract));
        Contract savedContract = toDomain(contractEntity);

        return ContractCreateResponse.of(savedContract.getCode());
    }

    @Transactional
    public ContractInfoResponse confirmContract(ContractConfirmRequest request) {
        ContractEntity contractEntity = contractRepository.findByCode(request.contractCode());
        Contract contract = toDomain(contractEntity);

        validateConfirm(request.xCode(), contract.getInfo());
        contract.confirm();

        ContractMapper.applyToEntity(contract, contractEntity);

        contractRepository.saveContract(contractEntity);

        applicationEventPublisher.publishEvent(new ContractConfirmEvent(contract.getCode(), contract.getCreatedAt()));

        return ContractInfoResponse.of(contract.getCode(), contract.getInfo().status().name());
    }

    private void isValidMember(List<String> memberCodes) {
        URI memberExistsUrl = createMemberInfoUrl(memberCodes);
        MemberInfoResponse memberInfoResponse = Optional.ofNullable(restTemplate.getForObject(memberExistsUrl, MemberInfoResponse.class))
                .orElseThrow(() -> new ContractException(INVALID_MEMBER));

        List<MemberInfo> memberInfos = memberInfoResponse.members();

        if (memberInfos.size() != memberCodes.size()) {
            throw new ContractException(INVALID_MEMBER);
        }

        if (noFreelancer(memberInfos)) {
            throw new ContractException(NO_FREELANCERS);
        }
    }

    private boolean noFreelancer(List<MemberInfo> memberInfos) {
        return memberInfos.stream()
                .filter(MemberInfo::canWork)
                .findFirst()
                .isEmpty();
    }

    private URI createMemberInfoUrl(List<String> memberCodes) {
        return UriComponentsBuilder.newInstance()
                .scheme("lb")
                .host(memberServiceName)
                .path(memberInfoUrl)
                .queryParam("member-code", memberCodes)
                .build()
                .toUri();
    }

    private void validateConfirm(String xCode, ContractInfo info) {
        isValidMember(List.of(info.requestorCode(), info.contractorCode()));

        if (!xCode.equals(info.contractorCode())) {
            throw new ContractException(NOT_CONTRACTOR);
        }

        if (info.status() != ContractStatus.REQUESTED) {
            throw new ContractException(NOT_REQUESTED_STATUS);
        }
    }
}
