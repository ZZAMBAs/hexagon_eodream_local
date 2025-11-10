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

    public Settlement(String code, SettlementReference settlementReference,
        SettlementStatusInfo settlementStatusInfo, SettlementTimeline settlementTimeline) {
        this.code = (code == null) ? generateCode() : code;
        this.settlementReference = settlementReference;
        this.settlementStatusInfo = settlementStatusInfo;
        this.settlementTimeline = settlementTimeline;
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }
}
