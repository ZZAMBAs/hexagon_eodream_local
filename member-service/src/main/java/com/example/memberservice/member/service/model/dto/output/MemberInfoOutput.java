package com.example.memberservice.member.service.model.dto.output;

import com.example.memberservice.member.service.model.vo.MemberInfo;
import java.util.List;

public record MemberInfoOutput(
    List<MemberInfo> memberInfos
) {

}
