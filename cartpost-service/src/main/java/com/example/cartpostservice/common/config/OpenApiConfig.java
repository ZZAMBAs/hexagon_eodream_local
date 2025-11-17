package com.example.cartpostservice.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .title("CartPost API")
                .version("v1.0.0")
                .description("장바구니와 게시글의 API 명세서입니다.");

        return new OpenAPI()
                .info(info);
    }
}
