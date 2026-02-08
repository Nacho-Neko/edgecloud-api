-- V20260128.1630__alter_eop_bound_direction_to_int.sql
-- 将 eop_bound 表中的 direction 字段从 VARCHAR 修改为 INTEGER

-- 先转换现有的数据
-- INBOUND -> 1
-- OUTBOUND -> 2
ALTER TABLE eop_bound 
ALTER COLUMN direction TYPE INTEGER 
USING (CASE 
    WHEN direction = 'INBOUND' THEN 1 
    WHEN direction = 'OUTBOUND' THEN 2 
    ELSE NULL 
END);
