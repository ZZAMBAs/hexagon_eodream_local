package com.example.cartpostservice.commissions.service;

import com.example.cartpostservice.commissions.model.CommissionsEntity;
import com.example.cartpostservice.commissions.repository.CommissionsRepository;
import com.example.cartpostservice.commissions.service.dto.request.CommissionsServiceCommand;
import com.example.cartpostservice.commissions.service.dto.response.CommissionsServiceResult;
import com.example.cartpostservice.common.exception.BusinessException;
import com.example.cartpostservice.common.exception.CustomStatusCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommissionsService implements CrudService<CommissionsServiceCommand, CommissionsServiceResult, String> {

    private final CommissionsRepository commissionsRepository;

    @Override
    public String create(CommissionsServiceCommand requestDto) {
        CommissionsEntity commissions = CommissionsEntity.builder()
                .memberCode(requestDto.memberCode())
                .title(requestDto.title())
                .content(requestDto.content())
                .paymentType(requestDto.paymentType())
                .unitAmount(requestDto.unitAmount())
                .startedAt(requestDto.startedAt())
                .endedAt(requestDto.endedAt())
                .writerName(requestDto.writerName())
                .build();

        CommissionsEntity saved = commissionsRepository.save(commissions);

        return saved.getCode();
    }

    @Override
    public CommissionsServiceResult read(String commissionsCode) {

        CommissionsEntity commission = commissionsRepository.findByCode(commissionsCode)
                .orElseThrow(() -> new BusinessException(CustomStatusCode.NOT_FOUND_COMMISSION));

        CommissionsServiceResult result = new CommissionsServiceResult(
                commission.getCode(),
                commission.getMemberCode(),
                commission.getTitle(),
                commission.getContent(),
                commission.getPaymentType(),
                commission.getUnitAmount(),
                commission.getStartedAt(),
                commission.getEndedAt(),
                commission.isOpen(),
                commission.getWriterName()
        );

        return result;
    }

    @Override
    public void update(CommissionsServiceCommand requestDto, String commissionsCode) {
        List<CommissionsEntity> commissions = commissionsRepository.findByMemberCode(requestDto.memberCode());
        if (commissions.isEmpty()) {
            throw new BusinessException(CustomStatusCode.NOT_FOUND_COMMISSION);
        }
        boolean owned = commissions.stream().anyMatch(entity -> entity.getCode().equals(commissionsCode));

        if (!owned) {
            throw new BusinessException(CustomStatusCode.FORBIDDEN_COMMISSION);
        }

        CommissionsEntity commission = CommissionsEntity.builder()
                .memberCode(requestDto.memberCode())
                .title(requestDto.title())
                .content(requestDto.content())
                .paymentType(requestDto.paymentType())
                .unitAmount(requestDto.unitAmount())
                .startedAt(requestDto.startedAt())
                .endedAt(requestDto.endedAt())
                .writerName(requestDto.writerName())
                .build();

        CommissionsEntity saved = commissionsRepository.save(commission);
    }

    @Override
    public void delete(String memberCode, String commissionsCode) {
        CommissionsEntity commission = commissionsRepository.findByMemberCodeAndCode(memberCode, commissionsCode)
                .orElseThrow(() -> new BusinessException(CustomStatusCode.FORBIDDEN_COMMISSION));

        commissionsRepository.delete(commission);
    }

    public Page<CommissionsServiceResult> getPage(String memberCode, Pageable pageable) {

        Page<CommissionsEntity> commissions = commissionsRepository.findPageByMemberCode(memberCode, pageable);

        return commissions.map(commission ->
                new CommissionsServiceResult(
                        commission.getCode(),
                        commission.getMemberCode(),
                        commission.getTitle(),
                        commission.getContent(),
                        commission.getPaymentType(),
                        commission.getUnitAmount(),
                        commission.getStartedAt(),
                        commission.getEndedAt(),
                        commission.isOpen(),
                        commission.getWriterName()
                )
        );
    }

    public boolean isOwner(String memberCode, String commissionsCode) {
        return commissionsRepository
                .findByMemberCodeAndCode(memberCode, commissionsCode)
                .isPresent();
    }

    public void closeCommission(String commissionCode) {
        CommissionsEntity entity = commissionsRepository.findByCode(commissionCode)
                .orElseThrow(() -> new BusinessException(CustomStatusCode.FORBIDDEN_COMMISSION));

        entity.closed();
    }
}