package com.example.contractservice.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Springdoc OpenAPI UI 설정을 위한 클래스입니다.
 */
@OpenAPIDefinition(
    servers = {
        @Server(url = "http://localhost:8000", description = "Gateway URL")
    }
)
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("계약, 정산, 예치금 관리 모듈의 API")
                .version("v1.0.0")
                .description("계약/정산/예치금 관리 모듈의 API 명세서입니다. 모듈 간 통신을 위한 API는 /internal을 포함합니다.");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
