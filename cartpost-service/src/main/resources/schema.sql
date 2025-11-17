CREATE TABLE IF NOT EXISTS `payments` (
                                          `id` BIGINT NOT NULL,
                                          `member_code` CHAR(36) NOT NULL,
    `code` CHAR(36) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `is_deleted` BOOLEAN NOT NULL,
    `total_amount` BIGINT NOT NULL,
    `payment_key` VARCHAR(255) NOT NULL,
    `pg_order_id` VARCHAR(255) NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `members` (
                                         `id` BIGINT NOT NULL,
                                         `code` CHAR(36) NOT NULL COMMENT '외부 노출용 식별자(UUID)',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정 일시',
    `is_deleted` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '삭제 여부',
    `name` VARCHAR(255) NOT NULL COMMENT '이름',
    `email` VARCHAR(255) NOT NULL COMMENT '이메일',
    `phone_number` VARCHAR(20) NOT NULL COMMENT '핸드폰 번호',
    `birth_date` DATE NOT NULL COMMENT '생년월일',
    `gender` VARCHAR(10) NOT NULL COMMENT '성별',
    `provider` VARCHAR(20) NOT NULL COMMENT 'OAuth 제공자',
    `provider_id` VARCHAR(255) NOT NULL COMMENT 'OAuth 서버 제공 ID',
    `can_work` BOOLEAN NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `experiences` (
                                             `id` BIGINT NOT NULL,
                                             `resume_code` CHAR(36) NOT NULL,
    `code` CHAR(36) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `is_deleted` BOOLEAN NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `organization` VARCHAR(255) NOT NULL,
    `description` TEXT NOT NULL,
    `started_at` DATE NOT NULL,
    `ended_at` DATE NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `RefreshToken` (
                                              `member_code` VARCHAR(36) NOT NULL,
    `refresh_token` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`member_code`)
    );

CREATE TABLE IF NOT EXISTS `tags` (
                                      `id` BIGINT NOT NULL,
                                      `code` CHAR(36) NOT NULL,
    `skill` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `settlements` (
                                             `id` BIGINT NOT NULL,
                                             `receiver_code` CHAR(36) NOT NULL,
    `contract_code` CHAR(36) NOT NULL,
    `code` CHAR(36) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `settled_at` TIMESTAMP NOT NULL,
    `original_amount` BIGINT NOT NULL,
    `settlement_rate` DECIMAL(5,2) NOT NULL,
    `settlement_amount` BIGINT NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    `progressing_at` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `chat_message` (
                                              `id` VARCHAR(36) NOT NULL,
    `sender_code` CHAR(36) NOT NULL,
    `chat_room_id` VARCHAR(36) NOT NULL,
    `content` VARCHAR(1000) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `commissions` (
    `id` BIGINT NOT NULL,
    `member_code` CHAR(36) NOT NULL,
    `code` CHAR(36) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `is_deleted` BOOLEAN NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `content` TEXT NOT NULL,
    `payment_type` VARCHAR(20) NOT NULL,
    `unit_amount` BIGINT NOT NULL,
    `started_at` DATE NOT NULL,
    `ended_at` DATE NOT NULL,
    `is_open` BOOLEAN NOT NULL,
    `writer_name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `deposit_histories` (
                                                   `id` BIGINT NOT NULL,
                                                   `deposit_code` CHAR(36) NOT NULL,
    `code` CHAR(36) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `is_deleted` BOOLEAN NOT NULL,
    `change_amount` BIGINT NOT NULL,
    `summary` VARCHAR(255) NOT NULL,
    `result_amount` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `members_tags` (
                                              `id` BIGINT NOT NULL,
                                              `member_code` CHAR(36) NOT NULL,
    `tag_code` CHAR(36) NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `chat_rooms` (
                                            `id` VARCHAR(36) NOT NULL,
    `client_code` CHAR(36) NOT NULL,
    `freelancer_code` CHAR(36) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `ratings` (
                                         `id` BIGINT NOT NULL,
                                         `receiver_code` CHAR(36) NOT NULL,
    `satisfied_count` INT NOT NULL,
    `unsatisfied_count` INT NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `carts` (
                                       `id` BIGINT NOT NULL,
                                       `code` CHAR(36) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL,
    `is_deleted` BOOLEAN NOT NULL,
    `member_code` CHAR(36) NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `cart_items` (
                                            `id` BIGINT NOT NULL,
                                            `contract_code` CHAR(36) NOT NULL,
    `cart_code` CHAR(36) NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `contracts` (
                                           `id` BIGINT NOT NULL,
                                           `requestor_code` CHAR(36) NOT NULL,
    `contractor_code` CHAR(36) NOT NULL,
    `code` CHAR(36) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `is_deleted` BOOLEAN NOT NULL,
    `started_at` TIMESTAMP NOT NULL,
    `ended_at` TIMESTAMP NOT NULL,
    `payment_type` VARCHAR(20) NOT NULL,
    `unit_amount` BIGINT NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `body` TEXT NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `social_members` (
                                                `id` BIGINT NOT NULL,
                                                `code` CHAR(36) NOT NULL COMMENT '외부 노출용 식별자(UUID)',
    `created_at` TIMESTAMP NOT NULL COMMENT '생성 일시',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정 일시',
    `is_deleted` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '삭제 여부',
    `email` VARCHAR(255) NOT NULL COMMENT '이메일',
    `provider` VARCHAR(20) NOT NULL COMMENT 'OAuth 제공자',
    `provider_id` VARCHAR(255) NOT NULL COMMENT 'OAuth 서버 제공 ID',
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `commissions_tags` (
                                                  `id` BIGINT NOT NULL,
                                                  `commission_code` CHAR(36) NOT NULL,
    `tag_code` CHAR(36) NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `self_promotions` (
                                                 `id` BIGINT NOT NULL,
                                                 `member_code` CHAR(36) NOT NULL,
    `resume_code` CHAR(36) NOT NULL,
    `code` CHAR(36) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `is_deleted` BOOLEAN NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `content` TEXT NOT NULL,
    `payment_type` VARCHAR(20) NOT NULL,
    `unit_amount` BIGINT NOT NULL,
    `writer_name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `resumes` (
                                         `id` BIGINT NOT NULL,
                                         `member_code` CHAR(36) NOT NULL,
    `code` CHAR(36) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `is_deleted` BOOLEAN NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `body` TEXT NOT NULL,
    `link` VARCHAR(255),
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `deposits` (
                                          `id` BIGINT NOT NULL,
                                          `member_code` CHAR(36) NOT NULL,
    `code` CHAR(36) NOT NULL,
    `amount` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
    );
