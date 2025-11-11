package com.example.memberservice.oauth.controller.swagger;

import com.example.memberservice.common.model.vo.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "OAuth(인증) API", description = "로그아웃, AccessToken 재발급 등")
public interface OAuthApiControllerSwagger {

    @PostMapping("/reissue")
    @Operation(summary = "AccessToken 재발급 Api", description = "RefreshToken을 통해 AccessToken을 재발급합니다.")
    ResponseEntity<ResponseDto<Null>> reissueAccessTokenByRefreshToken(
        @CookieValue("refresh-token") String refreshToken);


    @DeleteMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "관리 중이던 Refresh Token을 제거해주어 로그아웃 시킵니다.")
    ResponseEntity<ResponseDto<Null>> logoutMemberByRefreshToken(
        @CookieValue("refresh-token") String refreshToken);
}
