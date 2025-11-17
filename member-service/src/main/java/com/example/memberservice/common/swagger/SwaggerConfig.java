package com.example.memberservice.common.swagger;

import com.example.memberservice.common.exception.ErrorCode;
import com.example.memberservice.common.swagger.adaptor.ApiErrorResponsesAdaptor;
import com.example.memberservice.common.swagger.annotation.ApiErrorResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OperationCustomizer;

@Configuration
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class SwaggerConfig implements WebMvcConfigurer {

    private final ApiErrorResponsesAdaptor apiErrorResponsesAdaptor;


    @Bean
    public OpenAPI openAPI() {

        OpenAPI openAPI = new OpenAPI()
            .components(new Components()) // 초기 Components
            .info(apiInfo());

        return openAPI;
    }


    private Info apiInfo() {
        return new Info()
            .title("백엔드 단기심화 1기 1팀 헥사곤 멤버 모듈 Swagger 문서") // API의 제목
            .description("멤버 모듈 관련 명세서 종류입니다.") // API에 대한 설명
            .version("1.0.0"); // API의 버전
    }

    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> {
            ApiErrorResponses apiErrorResponses =
                handlerMethod.getMethodAnnotation(ApiErrorResponses.class);

            if (apiErrorResponses != null) {
                ErrorCode[] exceptions = apiErrorResponses.exceptions();
                if (exceptions.length == 1) {
                    apiErrorResponsesAdaptor.generateErrorCodeResponseExample(operation, exceptions[0]);
                } else {
                    apiErrorResponsesAdaptor.generateErrorCodeResponseExample(operation, exceptions);
                }
            }

            return operation;
        };
    }
}
