package com.example.memberservice.common.security.jwt;

import com.example.memberservice.common.exception.BusinessException;
import com.example.memberservice.common.exception.BusinessCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenParser {

    private final JwtProperties jwtProperties;

    public String parseMemberCode(Claims claims) {
        Object memberCode = claims.get(jwtProperties.getMemberCodeClaims());
        if (memberCode == null) {
            throw new BusinessException(BusinessCode.UNAUTHORIZATION);
        }
        return memberCode.toString();
    }

}
