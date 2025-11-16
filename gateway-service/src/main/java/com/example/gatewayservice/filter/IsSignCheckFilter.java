package com.example.gatewayservice.filter;

import com.example.gatewayservice.common.exception.BusinessException;
import com.example.gatewayservice.common.exception.ErrorCode;
import com.example.gatewayservice.common.jwt.JwtTokenParser;
import com.example.gatewayservice.filter.IsSignCheckFilter.Config;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class IsSignCheckFilter extends AbstractGatewayFilterFactory<Config> {

    private final JwtTokenParser jwtTokenParser;

    public IsSignCheckFilter(JwtTokenParser jwtTokenParser) {
        super(Config.class);
        this.jwtTokenParser = jwtTokenParser;
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            Claims claims = (Claims) exchange.getAttribute("claims");

            if (claims == null) {
                throw new BusinessException(ErrorCode.UNAUTHORIZATION);
            }

            Boolean isSign = jwtTokenParser.parseIsSign(claims);
            if (!Boolean.TRUE.equals(isSign)) {
                throw new BusinessException(ErrorCode.NEED_SIGNUP);
            }

            return chain.filter(exchange);
        };
    }


}
