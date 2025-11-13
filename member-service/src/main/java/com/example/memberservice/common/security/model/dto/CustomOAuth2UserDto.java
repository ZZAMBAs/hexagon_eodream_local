package com.example.memberservice.common.security.model.dto;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomOAuth2UserDto implements OAuth2User {

    private final OAuth2User oAuth2User;

    private final UserInfoDto userInfoDto;

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    public String getMemberCode() {
        return this.userInfoDto.memberCode();
    }
}
