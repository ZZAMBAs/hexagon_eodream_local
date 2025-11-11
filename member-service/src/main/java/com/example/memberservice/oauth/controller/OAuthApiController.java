package com.example.memberservice.oauth.controller;

import com.example.memberservice.common.model.vo.ResponseDto;
import com.example.memberservice.oauth.controller.swagger.OAuthApiControllerSwagger;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class OAuthApiController implements OAuthApiControllerSwagger {


    @PostMapping("/reissue")
    public ResponseEntity<ResponseDto<Null>> reissueAccessTokenByRefreshToken(
        @CookieValue("refresh-token") String refreshToken) {

        return null;
    }


    @DeleteMapping("/logout")
    public ResponseEntity<ResponseDto<Null>> logoutMemberByRefreshToken(
        @CookieValue("refresh-token") String refreshToken) {

        return null;
    }

}
