package com.example.memberservice.member.controller;

import com.example.memberservice.common.model.vo.ResponseDto;
import com.example.memberservice.member.controller.dto.request.UserCreateRequest;
import com.example.memberservice.member.controller.dto.request.UserUpdateRequest;
import com.example.memberservice.member.controller.dto.response.UserGetResponse;
import com.example.memberservice.member.controller.swagger.MemberApiControllerSwagger;
import com.example.memberservice.member.service.MemberService;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    public ResponseEntity<ResponseDto<UserGetResponse>> getMemberById(
        @RequestParam(name = "member-code", required = false) String memberCode) {

        return null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseDto<Null>> createUser(@RequestBody UserCreateRequest request) {

        return ResponseEntity.status(200).body(new ResponseDto<>(200, "sss", null));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ResponseDto<Null>> updateUser(@RequestBody UserUpdateRequest request) {

        return null;
    }

    @PatchMapping("/state")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ResponseDto<Null>> updateUserWorkState(@RequestHeader("X-CODE") String memberCode) {

        return null;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ResponseDto<Null>> deleteUser() {

        return null;
    }

    @GetMapping("/check-name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Null> existMemberByName(@RequestParam(name = "name") String name) {

        return null;
    }


}
