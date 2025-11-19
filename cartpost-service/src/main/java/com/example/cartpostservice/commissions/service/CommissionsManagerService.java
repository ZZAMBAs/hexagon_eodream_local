package com.example.cartpostservice.commissions.service;

import com.example.cartpostservice.commissions.controller.dto.request.CommissionUpsertRequest;
import com.example.cartpostservice.commissions.controller.dto.response.CommissionCreateResponse;
import com.example.cartpostservice.commissions.controller.dto.response.CommissionElementReadResponse;
import com.example.cartpostservice.commissions.controller.dto.response.CommissionUpdateResponse;
import com.example.cartpostservice.commissions.controller.dto.response.CommissionReadResponse;
import com.example.cartpostservice.commissions.controller.dto.response.MemberResponse;
import com.example.cartpostservice.commissions.controller.internal.MemberClient;
import com.example.cartpostservice.commissions.service.dto.request.CommissionsServiceCommand;
import com.example.cartpostservice.commissions.service.dto.request.TagServiceCommand;
import com.example.cartpostservice.commissions.service.dto.response.CommissionsServiceResult;
import com.example.cartpostservice.commissions.service.dto.response.TagServiceResult;
import com.example.cartpostservice.common.exception.BusinessException;
import com.example.cartpostservice.common.exception.CustomStatusCode;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommissionsManagerService {

    private final CommissionsService commissionsService;
    private final CommissionsTagService commissionsTagService;
    private final MemberClient memberClient;

    @Transactional
    public CommissionCreateResponse createCommission(String memberCode, CommissionUpsertRequest request) {

        MemberResponse member = memberClient.getMember(memberCode);

        // request에서 온 것을 커미션과 태그 용 리퀘스트로 분리
        CommissionsServiceCommand commissionsServiceCommand = new CommissionsServiceCommand(
                memberCode,
                request.title(),
                request.content(),
                request.paymentType(),
                request.unitAmount(),
                request.startedAt(),
                request.endedAt(),
                member.nickName()
        );

        // 커미션 서비스에 리퀘스트 데이터를 저장 데이터 받기
        String commissionsCode = commissionsService.create(commissionsServiceCommand);

        // 태그 서비스에 리퀘스트 데이터 저장
        TagServiceCommand tagServiceCommand = new TagServiceCommand(
                commissionsCode,
                request.tagCode()
        );

        commissionsTagService.create(tagServiceCommand);

        // 응답 데이터에 commissionscode 전달
        CommissionCreateResponse commissionCreateResponse = new CommissionCreateResponse(commissionsCode);

        return commissionCreateResponse;
    }

    @Transactional
    public CommissionElementReadResponse readCommission(String commissionCode) {

        CommissionsServiceResult commissionResult = commissionsService.read(commissionCode);
        TagServiceResult tagResult = commissionsTagService.read(commissionResult.code());

        CommissionElementReadResponse response = new CommissionElementReadResponse(
                commissionResult.title(),
                commissionResult.content(),
                commissionResult.paymentType(),
                commissionResult.unitAmount(),
                commissionResult.startedAt(),
                commissionResult.endedAt(),
                commissionResult.isOpen(),
                commissionResult.writerName(),
                tagResult.tagCodes()
        );

        return response;
    }

    @Transactional
    public CommissionUpdateResponse updateCommission(String code, String commissionCode,
            CommissionUpsertRequest request) {

        MemberResponse member = memberClient.getMember(code);

        CommissionsServiceCommand commissionsServiceCommand = new CommissionsServiceCommand(
                code,
                request.title(),
                request.content(),
                request.paymentType(),
                request.unitAmount(),
                request.startedAt(),
                request.endedAt(),
                member.nickName()
        );

        TagServiceCommand tagServiceCommand = new TagServiceCommand(
                commissionCode,
                request.tagCode()
        );

        commissionsService.update(commissionsServiceCommand, commissionCode);
        commissionsTagService.update(tagServiceCommand, commissionCode);

        return new CommissionUpdateResponse(commissionCode);
    }

    @Transactional
    public void deleteCommission(String code, String commissionCode) {

        commissionsService.delete(code, commissionCode);
        commissionsTagService.delete(code, commissionCode);
    }

    @Transactional
    public void finishCommission(String code, String commissionCode) {
        if (!commissionsService.isOwner(code, commissionCode)) {
            throw new BusinessException(CustomStatusCode.FORBIDDEN_COMMISSION);
        }

        commissionsService.closeCommission(commissionCode);
    }

    @Transactional
    public Page<CommissionReadResponse> readOwnCommissions(String code, Pageable pageable) {

        int page = 0;
        if (pageable.getPageNumber() > 0) {
            page = pageable.getPageNumber() - 1;
        }

        Pageable adjustedPageable = PageRequest.of(
                page,
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<CommissionsServiceResult> resultPage = commissionsService.getPage(code, adjustedPageable);

        List<String> commissionCodes = resultPage.stream()
                .map(CommissionsServiceResult::code)
                .toList();

        List<TagServiceResult> tagServiceResults = commissionsTagService.getTags(commissionCodes);

        Map<String, List<String>> tagMap = tagServiceResults.stream()
                .collect(Collectors.toMap(
                        TagServiceResult::commissionCode,
                        TagServiceResult::tagCodes
                ));

        List<CommissionReadResponse> responses = resultPage.stream()
                .map(result -> new CommissionReadResponse(
                        result.title(),
                        result.paymentType(),
                        result.unitAmount(),
                        result.startedAt(),
                        result.endedAt(),
                        result.isOpen(),
                        result.writerName(),
                        tagMap.getOrDefault(result.code(), List.of())
                ))
                .toList();

        return new PageImpl<>(responses, pageable, resultPage.getTotalElements());
    }

    @Transactional
    public void canAccessCommission(String code, String commissionCode) {
        if (!commissionsService.isOwner(code, commissionCode)) {
            throw new BusinessException(CustomStatusCode.FORBIDDEN_COMMISSION);
        }
    }
}
