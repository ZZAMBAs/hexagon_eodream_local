CREATE TABLE IF NOT EXISTS orders (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              code VARCHAR(255) NOT NULL UNIQUE,
                              is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                              created_at TIMESTAMP,
                              updated_at TIMESTAMP,
                              member_code VARCHAR(255) NOT NULL,
                              order_pg_id VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS payments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          code VARCHAR(255) NOT NULL UNIQUE,
                          is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                          created_at TIMESTAMP,
                          updated_at TIMESTAMP,
                          order_pg_id VARCHAR(255) NOT NULL UNIQUE,
                          payment_key VARCHAR(255),
                          amount BIGINT NOT NULL,
                          payment_status VARCHAR(255) NOT NULL
);

