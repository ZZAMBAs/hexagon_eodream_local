package com.example.memberservice.member.controller;

import com.example.memberservice.common.model.vo.ResponseDto;
import com.example.memberservice.member.controller.dto.vo.MemberInfo;
import com.example.memberservice.member.controller.swagger.MemberInternalControllerSwagger;
import com.example.memberservice.member.service.MemberInternalService;
import com.example.memberservice.member.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/members")
@RequiredArgsConstructor
public class MemberInternalController implements MemberInternalControllerSwagger {

    // GET /?member-code
    // GET /exist?member-code

    private final MemberInternalService memberInternalService;

    @GetMapping()
    public ResponseEntity<ResponseDto<MemberInfo>> getMemberInfoByCode(
        @RequestParam(name = "member-code", required = false) List<String> paramMemberCode) {

        //TODO 응답은 이후에 구성
        return null;
    }

    @GetMapping("/exist")
    public ResponseEntity<ResponseDto<MemberInfo>> existMemberByCode(
        @RequestParam(name = "member-code", required = false) String paramMemberCode) {

        //TODO 응답은 이후에 구성
        return null;
    }
}
