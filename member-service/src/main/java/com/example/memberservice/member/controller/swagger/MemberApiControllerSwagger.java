package com.example.memberservice.member.controller.swagger;

import com.example.memberservice.common.web.model.vo.Empty;
import com.example.memberservice.common.web.model.dto.ResponseDto;
import com.example.memberservice.member.controller.dto.request.UserCreateRequest;
import com.example.memberservice.member.controller.dto.request.UserUpdateRequest;
import com.example.memberservice.member.controller.dto.response.UserGetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Member API", description = "사용자 관련 API를 제공합니다.")
public interface MemberApiControllerSwagger {


    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회합니다.")
    @Parameters({
        @Parameter(name = "X-CODE", description = "로그인 사용자 코드", in = ParameterIn.HEADER, required = true),
        @Parameter(name = "member-code", description = "검색할 사용자 Code(없을 경우 본인 정보 검색)", in = ParameterIn.QUERY)
    })
    ResponseDto<UserGetResponse> getMemberById(
        @RequestParam(name = "member-code", required = false) String memberCode);

    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
    @Parameters({
        @Parameter(name = "X-CODE", description = "로그인 사용자 코드", in = ParameterIn.HEADER, required = true)
    })
    ResponseDto<Empty> createUser(@RequestBody UserCreateRequest request);

    @Operation(summary = "사용자 정보 수정", description = "사용자 정보를 수정합니다.")
    @Parameters({
        @Parameter(name = "X-CODE", description = "로그인 사용자 코드", in = ParameterIn.HEADER, required = true)
    })
    ResponseDto<Empty> updateUser(@RequestBody UserUpdateRequest request);

    @Operation(summary = "사용자 판매자 등록", description = "사용자의 판매자 등록을 진행합니다")
    @Parameters({
        @Parameter(name = "X-CODE",description = "로그인한 사용자 코드",in = ParameterIn.HEADER, required = true)
    })
    ResponseDto<Empty> updateUserWorkState(@RequestHeader("X-CODE") String memberCode);

    @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
    @Parameters({
        @Parameter(name = "X-CODE", description = "로그인 사용자 코드", in = ParameterIn.HEADER, required = true)
    })
    ResponseDto<Empty> deleteUser();

    @Operation(summary = "사용자 이름 중복 체크", description = "사용자 이름이 이미 존재하는지 확인합니다.")
    @Parameters({
        @Parameter(name = "X-CODE", description = "로그인 사용자 코드", in = ParameterIn.HEADER, required = true),
        @Parameter(name = "name", description = "체크할 사용자 이름", in = ParameterIn.QUERY, required = true)
    })
    ResponseDto<Empty> existMemberByName(@RequestParam(name = "name") String name);
}
