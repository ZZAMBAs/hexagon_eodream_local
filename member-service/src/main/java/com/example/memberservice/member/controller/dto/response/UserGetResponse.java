package com.example.memberservice.member.controller.dto.response;

import com.example.memberservice.member.controller.dto.vo.MemberInfo;
import com.example.memberservice.member.controller.dto.vo.MemberRating;
import com.example.memberservice.member.controller.dto.vo.MemberTag;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record UserGetResponse(
    @Schema(description = "사용자 정보")
    MemberInfo info,
    
    @Schema(description = "사용자 평가 정보")
    MemberRating rating,

    @Schema(description = "사용자 기술 태그 정보")
    List<MemberTag> tags
) {

}
