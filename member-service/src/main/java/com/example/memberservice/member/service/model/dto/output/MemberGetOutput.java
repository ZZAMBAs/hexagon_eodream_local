package com.example.memberservice.member.service.model.dto.output;


import com.example.memberservice.member.service.model.vo.ApiMemberInfo;
import com.example.memberservice.member.service.model.vo.MemberRating;
import com.example.memberservice.member.service.model.vo.MemberTag;
import java.util.List;

public record MemberGetOutput(
    ApiMemberInfo info,

    MemberRating rating,

    List<MemberTag> tags
) {

}
