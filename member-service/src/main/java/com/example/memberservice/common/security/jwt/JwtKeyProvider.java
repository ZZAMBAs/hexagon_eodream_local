package com.example.memberservice.common.security.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtKeyProvider {

    @Value("${jwt.access-token.secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh-token.secret}")
    private String refreshTokenSecret;

    public SecretKey getAccessTokenSignKey() {
        return getSigningKey(accessTokenSecret);
    }

    public SecretKey getRefreshTokenSignKey() {
        return getSigningKey(refreshTokenSecret);
    }

    private SecretKey getSigningKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
