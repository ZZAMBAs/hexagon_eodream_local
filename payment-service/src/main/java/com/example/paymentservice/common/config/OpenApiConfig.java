package com.example.paymentservice.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .title("Payment API")
                .version("v1.0.0")
                .description("결제 API 명세서입니다.");

        return new OpenAPI()
                .info(info);
    }
}
