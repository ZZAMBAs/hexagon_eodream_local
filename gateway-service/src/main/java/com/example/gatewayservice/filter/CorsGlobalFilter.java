package com.example.gatewayservice.filter;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(-1)
@Component
public class CorsGlobalFilter implements GlobalFilter {

    @Value("${cors.allowed.origin}")
    private String allowedOrigin;

    private List<String> allowedOriginList;


    @PostConstruct
    public void init() {
        allowedOriginList = Arrays.asList(allowedOrigin.split(","));
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();

        String requestOrigin = exchange.getRequest().getHeaders().getOrigin();
        if (requestOrigin != null && allowedOriginList.contains(requestOrigin)) {
            headers.add("Access-Control-Allow-Origin", requestOrigin);
        }

        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Max-Age", "3600");

        // Preflight 요청 처리
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            response.setStatusCode(org.springframework.http.HttpStatus.OK);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }

}
