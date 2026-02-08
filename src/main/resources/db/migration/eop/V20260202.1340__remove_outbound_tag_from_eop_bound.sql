-- V20260202.1340__remove_outbound_tag_from_eop_bound.sql
-- 移除 eop_bound 表中的 outbound_tag 字段，因为该字段已移动到 extra_params_json 中

-- 1. 数据迁移：确保所有 outbound_tag 都已迁移到 extra_params_json 中（虽然代码层面已经同步，但为了安全可以进行数据修复）
-- 假设 extra_params_json 中如果没有 OutTag 键，则从 outbound_tag 字段补齐

-- 2. 移除字段
ALTER TABLE eop_bound DROP COLUMN IF EXISTS out_tag;
