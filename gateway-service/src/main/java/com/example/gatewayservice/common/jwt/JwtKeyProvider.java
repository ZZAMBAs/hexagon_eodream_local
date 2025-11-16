package com.example.gatewayservice.common.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtKeyProvider {

    @Value("${jwt.access-token.secret}")
    private String accessTokenSecret;

    public SecretKey getAccessTokenSignKey() {
        return getSigningKey(accessTokenSecret);
    }

    private SecretKey getSigningKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
