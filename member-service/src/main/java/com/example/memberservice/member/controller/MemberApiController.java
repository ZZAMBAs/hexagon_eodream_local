package com.example.memberservice.member.controller;

import com.example.memberservice.common.web.model.vo.Empty;
import com.example.memberservice.common.web.model.dto.ResponseDto;
import com.example.memberservice.member.controller.dto.request.UserCreateRequest;
import com.example.memberservice.member.controller.dto.request.UserUpdateRequest;
import com.example.memberservice.member.controller.dto.response.UserGetResponse;
import com.example.memberservice.member.controller.swagger.MemberApiControllerSwagger;
import com.example.memberservice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberApiController implements MemberApiControllerSwagger {

    //api 명세
    // POST /
    // PATCH /
    // DELETE /
    // GET /?member-code
    // GET /check-name?name

    private final MemberService memberService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<UserGetResponse> getMemberById(
        @RequestParam(name = "member-code", required = false) String memberCode) {

        return null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Empty> createUser(@RequestBody UserCreateRequest request) {

        return ResponseDto.success(HttpStatus.CREATED.value());
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Empty> updateUser(@RequestBody UserUpdateRequest request) {

        return ResponseDto.success();
    }

    @PatchMapping("/state")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Empty> updateUserWorkState(@RequestHeader("X-CODE") String memberCode) {

        return ResponseDto.success();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Empty> deleteUser() {

        return ResponseDto.success();
    }

    @GetMapping("/check-name")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Empty> existMemberByName(@RequestParam(name = "name") String name) {

        return ResponseDto.success();
    }


}
