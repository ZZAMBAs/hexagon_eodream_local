package com.example.memberservice.common.security.service;

import com.example.memberservice.common.model.vo.Provider;
import com.example.memberservice.common.security.model.dto.CustomOAuth2UserDto;
import com.example.memberservice.common.security.model.dto.UserInfoDto;
import com.example.memberservice.common.security.model.vo.GoogleUserInfo;
import com.example.memberservice.common.security.model.vo.KakaoUserInfo;
import com.example.memberservice.common.security.model.vo.NaverUserInfo;
import com.example.memberservice.common.security.model.vo.OAuthUserInfo;
import com.example.memberservice.socialmember.entity.SocialMembers;
import com.example.memberservice.socialmember.repository.SocialMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SocialMemberJpaRepository socialMemberRepository;

    @Override
    @Transactional
    //인증 서버에서 받아온 토큰으로 정보 서버에서 사용자 정보를 요청 및 후처리하는 과정입니다.
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("사용자 정보를 불러옵니다.");

        //해당 super.loadUser( ) 에서 정보 서버에서 사용자 정보를 요청 및 받아옵니다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("사용자 정보를 불러왔습니다.");

        String provider = userRequest.getClientRegistration().getClientName();

        OAuthUserInfo oAuthUserInfo = getOAuthUserInfo(provider, oAuth2User);

        String providerId = oAuthUserInfo.getProviderId();

        String memberEmail = oAuthUserInfo.getEmail();

        SocialMembers existMember = socialMemberRepository.findSocialMembersByProviderId(providerId).orElse(null);

        SocialMembers member = loadOrCreateSocialMember(existMember, provider, memberEmail, providerId);

        return new CustomOAuth2UserDto(oAuth2User,
            new UserInfoDto(member.getCode(), member.getProviderId(), member.getProvider()));
    }

    private SocialMembers loadOrCreateSocialMember(SocialMembers existMember, String provider, String memberEmail,
        String providerId) {
        SocialMembers member;
        //신규 가입의 경우
        if (existMember == null) {
            log.info("신규 회원입니다. 회원에 대한 정보를 저장합니다.");

            member = SocialMembers.builder()
                .provider(Provider.valueOf(provider.toUpperCase()))
                .email(memberEmail)
                .providerId(providerId)
                .build();

            member = socialMemberRepository.save(member);
            //이미 가입했던 유저의 경우
        } else {
            log.info("기존 유저입니다.");
            member = existMember;
        }

        log.info("PROVIDER : {}", provider);
        log.info("PROVIDER_ID : {}", providerId);
        log.info("Member_Code : {}", member.getCode());
        log.info("Member_Email : {}", memberEmail);

        return member;
    }

    private OAuthUserInfo getOAuthUserInfo(String provider, OAuth2User oAuth2User)
        throws OAuth2AuthenticationException {

        if (provider.equalsIgnoreCase("KAKAO")) {
            return new KakaoUserInfo(oAuth2User.getAttributes());
        } else if (provider.equalsIgnoreCase("GOOGLE")) {
            return new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (provider.equalsIgnoreCase("NAVER")) {
            return new NaverUserInfo(oAuth2User.getAttributes());
        }
        log.info("사용자 정보 불러오기에 실패했습니다.");
        throw new OAuth2AuthenticationException("사용자 정보 불러오기에 실패했습니다.");
    }
}
