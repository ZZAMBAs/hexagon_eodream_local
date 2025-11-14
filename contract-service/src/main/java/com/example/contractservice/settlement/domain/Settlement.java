package com.example.contractservice.settlement.domain;

import com.example.contractservice.settlement.domain.vo.SettlementReference;
import com.example.contractservice.settlement.domain.vo.SettlementStatusInfo;
import com.example.contractservice.settlement.domain.vo.SettlementTimeline;
import java.util.UUID;

public class Settlement {

    private String code;

    private SettlementReference settlementReference;

    private SettlementStatusInfo settlementStatusInfo;

    private SettlementTimeline settlementTimeline;

    public Settlement(SettlementReference settlementReference, SettlementStatusInfo settlementStatusInfo, SettlementTimeline settlementTimeline) {
        this(null, settlementReference, settlementStatusInfo, settlementTimeline);
    }
    public Settlement(String code, SettlementReference settlementReference,
        SettlementStatusInfo settlementStatusInfo, SettlementTimeline settlementTimeline) {
        this.code = (code == null) ? generateCode() : code;
        this.settlementReference = settlementReference;
        this.settlementStatusInfo = settlementStatusInfo;
        this.settlementTimeline = settlementTimeline;
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
}
