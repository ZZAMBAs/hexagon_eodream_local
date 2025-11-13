package com.example.memberservice.common.security.jwt;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

    private final JwtKeyProvider jwtKeyProvider;

    @Value("${jwt.access-token.ttl}")
    private long accessTokenTtl;

    @Value("${jwt.refresh-token.ttl}")
    private long refreshTokenTtl;

    public String generateAccessToken(String memberCode, boolean isSign) {
        return Jwts.builder()
            .claim("member-code", memberCode)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + accessTokenTtl))
            .signWith(jwtKeyProvider.getAccessTokenSignKey())
            .compact();
    }

    public String generateRefreshToken(String memberCode) {
        return Jwts.builder()
            .claim("member-code", memberCode)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + refreshTokenTtl))
            .signWith(jwtKeyProvider.getRefreshTokenSignKey())
            .compact();
    }
}
