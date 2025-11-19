package com.example.profileservice.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        // API 기본 정보 설정
        Info info = new Info()
                .title("Profile Service API (프리랜서 프로필 모듈)")
                .description("프리랜서의 이력서, 경력/경험, 프로모션, 기술 태그, 평가 정보를 관리하는 마이크로 서비스입니다.");


        // OpenAPI 객체 구성
        return new OpenAPI()
                .info(info);
    }
}
