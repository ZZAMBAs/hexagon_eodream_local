package com.example.contractservice.settlement.domain;

import static com.example.contractservice.settlement.domain.exception.SettlementErrorCode.FEE_NOT_CALCULATED;

import com.example.contractservice.settlement.common.SettlementStatus;
import com.example.contractservice.settlement.domain.exception.SettlementException;
import com.example.contractservice.settlement.domain.vo.SettlementReference;
import com.example.contractservice.settlement.domain.vo.SettlementStatusInfo;
import com.example.contractservice.settlement.domain.vo.SettlementTimeline;
import java.math.BigDecimal;
import java.util.UUID;

public class Settlement {
    private Long id;
    private String code;

    private SettlementReference settlementReference;

    private SettlementStatusInfo settlementStatusInfo;

    private SettlementTimeline settlementTimeline;

    public Settlement(Long id, String code, SettlementReference settlementReference,
        SettlementStatusInfo settlementStatusInfo, SettlementTimeline settlementTimeline) {
        this.id = id;
        this.code = (code == null) ? generateCode() : code;
        this.settlementReference = settlementReference;
        this.settlementStatusInfo = settlementStatusInfo;
        this.settlementTimeline = settlementTimeline;
    }

    public Settlement settle(BigDecimal settlementRate) {
        return new Settlement(id, code, settlementReference, settlementStatusInfo.settle(settlementRate), settlementTimeline.settle());
    }

    public Long getFee() {
        if (settlementStatusInfo.settledAmount() == null) {
            throw new SettlementException(FEE_NOT_CALCULATED);
        }

        return settlementStatusInfo.originalAmount() - settlementStatusInfo.settledAmount();
    }

    public Settlement fail() {
        return new Settlement(id, code, settlementReference, settlementStatusInfo.fail(), settlementTimeline.fail());
    }

    public boolean isValid() {
        return !hasNull() && !isTimelineInvalid() && !isInfoInvalid() && !isReferenceInvalid();
    }

    public boolean isFailed() {
        return settlementStatusInfo.status() == SettlementStatus.FAILED;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public SettlementReference getSettlementReference() {
        return settlementReference;
    }

    public SettlementStatusInfo getSettlementStatusInfo() {
        return settlementStatusInfo;
    }

    public SettlementTimeline getSettlementTimeline() {
        return settlementTimeline;
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }

    private boolean hasNull() {
        return settlementReference == null || settlementStatusInfo == null || settlementTimeline == null;
    }

    private boolean isTimelineInvalid() {
        return settlementTimeline.progressingAt() == null
                || settlementTimeline.failedAt() != null
                || settlementTimeline.settledAt() != null;
    }

    private boolean isInfoInvalid() {
        Long originalAmount = settlementStatusInfo.originalAmount();
        SettlementStatus status = settlementStatusInfo.status();

        return originalAmount == null || originalAmount < 0
                || status != SettlementStatus.BEFORE
                || settlementStatusInfo.settledAmount() != null;
    }

    private boolean isReferenceInvalid() {
        return settlementReference.contractCode() == null || settlementReference.receiverCode() == null;
    }
}
