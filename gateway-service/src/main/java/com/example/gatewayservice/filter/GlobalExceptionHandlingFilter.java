package com.example.gatewayservice.filter;


import com.example.gatewayservice.common.exception.BusinessException;
import com.example.gatewayservice.common.exception.ErrorCode;
import com.example.gatewayservice.common.web.model.dto.ResponseDto;
import com.example.gatewayservice.common.web.model.vo.Empty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class GlobalExceptionHandlingFilter
    extends AbstractGatewayFilterFactory<GlobalExceptionHandlingFilter.Config> {

    private final ObjectMapper om;

    public GlobalExceptionHandlingFilter(ObjectMapper om) {
        super(Config.class);
        this.om = om;
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> chain.filter(exchange)
            .onErrorResume(BusinessException.class, ex -> {
                ErrorCode errorCode = ex.getErrorCode();
                return writeErrorResponse(exchange.getResponse(), errorCode);
            });
    }

    private Mono<Void> writeErrorResponse(ServerHttpResponse response, ErrorCode errorCode) {
        response.setStatusCode(HttpStatus.valueOf(errorCode.getStatusCode()));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ResponseDto<Empty> body = new ResponseDto<>(
            errorCode.getCode(),
            errorCode.getStatusCode(),
            errorCode.getMessage(),
            Empty.getInstance()
        );

        byte[] bytes;
        try {
            bytes = om.writeValueAsBytes(body);
        } catch (JsonProcessingException e) {
            bytes = (
                "{\"code\":" + errorCode.getStatusCode() +
                    ",\"httpStatusCode\":" + errorCode.getStatusCode() +
                    ",\"message\":\"" + errorCode.getMessage() +
                    "\",\"data\":{\"errorCode\":\"" + errorCode.getCode() + "\"}}"
                    ).getBytes(StandardCharsets.UTF_8);
        }

        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }
}