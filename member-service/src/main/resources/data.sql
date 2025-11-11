
drop TABLE members;

CREATE TABLE members (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         code VARCHAR(36) NOT NULL COMMENT '외부 노출용 식별자(UUID)',
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
                         updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
                         is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '삭제 여부',
                         name VARCHAR(255) NOT NULL COMMENT '이름',
                         email VARCHAR(255) NOT NULL COMMENT '이메일',
                         phone_number VARCHAR(20) NOT NULL COMMENT '핸드폰 번호',
                         birth_date DATE NOT NULL COMMENT '생년월일',
                         gender ENUM('MALE', 'FEMALE') NOT NULL COMMENT '성별',
                         provider ENUM('GOOGLE', 'NAVER', 'KAKAO') NOT NULL COMMENT 'OAuth 제공자',
                         provider_id VARCHAR(255) NOT NULL COMMENT 'OAuth 서버 제공 ID',
                         can_work TINYINT(1) NOT NULL COMMENT '근무 가능 여부',
                         PRIMARY KEY (id),
                         UNIQUE KEY uq_code (code),
                         UNIQUE KEY uq_email (email)
);



CREATE TABLE `social_members` (
                                  `id` BIGINT NOT NULL AUTO_INCREMENT,
                                  `code` VARCHAR(36) NOT NULL COMMENT '외부 노출용 식별자(UUID)',
                                  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
                                  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
                                  `is_deleted` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '삭제 여부',
                                  `email` VARCHAR(255) NOT NULL COMMENT '이메일',
                                  `provider` ENUM('GOOGLE', 'NAVER', 'KAKAO') NOT NULL COMMENT 'OAuth 제공자',
                                  `provider_id` VARCHAR(255) NOT NULL COMMENT 'OAuth 서버 제공 ID',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `UK_code` (`code`),
                                  UNIQUE KEY `UK_provider_id` (`provider`, `provider_id`)
)