package com.example.memberservice.member.service.model.dto.output;

import com.example.memberservice.member.service.model.vo.InternalMemberInfo;
import java.util.List;

public record MemberInfoOutput(
    List<InternalMemberInfo> internalMemberInfos
) {

}
