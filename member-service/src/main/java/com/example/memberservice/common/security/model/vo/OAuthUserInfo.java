package com.example.memberservice.common.security.model.vo;

// 소셜로그인을 통해 받아오는 정보를 저장
// 이때 동의항목으로 Email만 받아오도록 했기에 Email 만 존재.
// -> 이름은 닉네임을 사용할 것이라 크게 중요하지 않을 것으로 판단.
public interface OAuthUserInfo {

    String getProviderId();

    String getProvider();

    String getEmail();
}
