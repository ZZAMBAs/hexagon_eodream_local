package com.example.profileservice.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing 설정을 분리하여 단위 테스트 시 불필요한 JPA 초기화를 방지합니다.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {

}
