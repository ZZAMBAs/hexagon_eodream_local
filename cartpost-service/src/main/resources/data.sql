-- commissions 테이블
MERGE INTO "commissions" KEY("id") VALUES
                                       (1, RANDOM_UUID(), 'c-1111-uuid', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'Commission A', 'Content for Commission A', 'MONTHLY', 100000, CURRENT_TIMESTAMP, DATEADD('DAY', 30, CURRENT_TIMESTAMP), TRUE, 'Alice'),
                                       (2, RANDOM_UUID(), 'c-2222-uuid', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'Commission B', 'Content for Commission B', 'PER_JOB', 200000, CURRENT_TIMESTAMP, DATEADD('DAY', 15, CURRENT_TIMESTAMP), TRUE, 'Bob'),
                                       (3, RANDOM_UUID(), 'c-3333-uuid', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'Commission C', 'Content for Commission C', 'MONTHLY', 150000, CURRENT_TIMESTAMP, DATEADD('DAY', 60, CURRENT_TIMESTAMP), FALSE, 'Charlie');

-- commissions_tags 테이블
MERGE INTO "commissions_tags" KEY("id") VALUES
                                            (1, 'c-1111-uuid', RANDOM_UUID()),
                                            (2, 'c-2222-uuid', RANDOM_UUID()),
                                            (3, 'c-3333-uuid', RANDOM_UUID());

-- carts 데이터 삽입
INSERT INTO carts (id, code, created_at, updated_at, is_deleted, member_code) VALUES
                                                                                  (1, 'cart-1111-uuid', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'member-uuid-001'),
                                                                                  (2, 'cart-2222-uuid', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'member-uuid-002'),
                                                                                  (3, 'cart-3333-uuid', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'member-uuid-003');

-- cart_items 데이터 삽입
INSERT INTO cart_items (id, code, contract_code, cart_code, status, started_at, ended_at, payment_type, amount) VALUES
                                                                                                                    (1, 'item-uuid-001', 'contract-uuid-001', 'cart-1111-uuid', 'CONFIRMED', CURRENT_TIMESTAMP, DATEADD('DAY', 30, CURRENT_TIMESTAMP), 'MONTHLY', 50000),
                                                                                                                    (2, 'item-uuid-002', 'contract-uuid-002', 'cart-2222-uuid', 'PAID', CURRENT_TIMESTAMP, DATEADD('DAY', 15, CURRENT_TIMESTAMP), 'ONE_TIME', 150000),
                                                                                                                    (3, 'item-uuid-003', 'contract-uuid-003', 'cart-3333-uuid', 'CONFIRMED', CURRENT_TIMESTAMP, DATEADD('DAY', 45, CURRENT_TIMESTAMP), 'MONTHLY', 75000);
