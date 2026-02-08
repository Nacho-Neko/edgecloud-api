-- V20260125.1600__alter_edge_area_code_nullable.sql
-- 修改 edge_area 表的 code 字段为可为空

ALTER TABLE edge_area ALTER COLUMN code DROP NOT NULL;
