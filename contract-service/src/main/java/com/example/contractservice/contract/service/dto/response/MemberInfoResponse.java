package com.example.contractservice.contract.service.dto.response;

import java.util.List;

public record MemberInfoResponse(
        List<MemberInfo> members
) {

    public record MemberInfo (
            String code,
            Boolean canWork
    ) {}
}
