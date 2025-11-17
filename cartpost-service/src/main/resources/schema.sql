
CREATE TABLE IF NOT EXISTS commissions (
    id BIGINT NOT NULL,
    member_code VARCHAR(36) NOT NULL,
    code VARCHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    payment_type VARCHAR(20) NOT NULL,
    unit_amount BIGINT NOT NULL,
    started_at TIMESTAMP NOT NULL,
    ended_at TIMESTAMP NOT NULL,
    is_open BOOLEAN NOT NULL,
    writer_name VARCHAR(255) NOT NULL,
    CONSTRAINT PK_COMMISSIONS PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS `commissions_tags` (
      `id` BIGINT NOT NULL,
      `commission_code` VARCHAR(36) NOT NULL,
      `tag_code` VARCHAR(36) NOT NULL,
      PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `carts` (
    `id` BIGINT NOT NULL,
    `code` VARCHAR(36) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL,
    `is_deleted` BOOLEAN NOT NULL,
    `member_code` VARCHAR(36) NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `cart_items` (
    `id` BIGINT NOT NULL,
    `code` VARCHAR(36) NOT NULL,
    `contract_code` VARCHAR(255) NOT NULL,
    `cart_code` VARCHAR(255) NOT NULL,
    `status` VARCHAR(50) NOT NULL,
    `started_at` TIMESTAMP NOT NULL,
    `ended_at` TIMESTAMP NOT NULL,
    `payment_type` VARCHAR(20) NOT NULL,
    `amount` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
);
