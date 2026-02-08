-- V20260128.1700__refactor_service_and_port_occupation.sql

-- 1. 处理 eop_service 表
-- 删除 outbound_work_id
ALTER TABLE eop_service DROP COLUMN IF EXISTS outbound_work_id;

-- 修改 status 字段类型为 INTEGER
-- 先将存量字符串映射为 ID
-- EMPTY -> 1 (映射到新的 INACTIVE)
-- INACTIVE -> 1
-- OUTBOUND_READY -> 2
-- ACTIVE -> 3
-- EXPIRED -> 4 (映射到新的 SUSPENDED)
ALTER TABLE eop_service ALTER COLUMN status DROP DEFAULT;

ALTER TABLE eop_service ALTER COLUMN status TYPE INTEGER USING (
    CASE status
        WHEN 'EMPTY' THEN 1
        WHEN 'INACTIVE' THEN 1
        WHEN 'OUTBOUND_READY' THEN 2
        WHEN 'ACTIVE' THEN 3
        WHEN 'EXPIRED' THEN 4
        ELSE 1
    END
);

ALTER TABLE eop_service ALTER COLUMN status SET DEFAULT 1;

-- 2. 处理 eop_port_occupation 表
-- 修改 occupier_type 字段类型为 INTEGER
-- EOP_APP -> 1
-- EOP_BOUND -> 2
-- Protocol -> 3
ALTER TABLE eop_port_occupation ALTER COLUMN occupier_type TYPE INTEGER USING (
    CASE occupier_type
        WHEN 'EOP_APP' THEN 1
        WHEN 'EOP_BOUND' THEN 2
        WHEN 'Protocol' THEN 3
        ELSE 1
    END
);
