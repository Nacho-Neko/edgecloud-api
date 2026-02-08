-- V20260130.1340__alter_eop_bound_max_connections_not_null.sql
-- 将 eop_bound 表中的 max_connections 列设为默认 128 且不能为空

-- 1. 更新现有为 NULL 的记录为 128
UPDATE eop_bound SET max_connections = 128 WHERE max_connections IS NULL;
UPDATE eop_product SET outbound_max_connections = 128 WHERE outbound_max_connections IS NULL;
UPDATE eop_product SET inbound_max_connections = 128 WHERE inbound_max_connections IS NULL;

-- 2. 修改列属性：增加 NOT NULL 约束，并设置默认值为 128
ALTER TABLE eop_bound ALTER COLUMN max_connections SET DEFAULT 128;
ALTER TABLE eop_bound ALTER COLUMN max_connections SET NOT NULL;

ALTER TABLE eop_product ALTER COLUMN outbound_max_connections SET DEFAULT 128;
ALTER TABLE eop_product ALTER COLUMN outbound_max_connections SET NOT NULL;

ALTER TABLE eop_product ALTER COLUMN inbound_max_connections SET DEFAULT 128;
ALTER TABLE eop_product ALTER COLUMN inbound_max_connections SET NOT NULL;
