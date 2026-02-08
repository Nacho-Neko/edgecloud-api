-- V20260130.1550__alter_edge_status_not_null.sql
-- 为 edge_status 表的监控指标字段添加非空约束

-- 1. 数据清洗：如果存在 NULL 值，更新为默认值 0
UPDATE edge_status SET cpu_usage = 0.0 WHERE cpu_usage IS NULL;
UPDATE edge_status SET used_memory = 0 WHERE used_memory IS NULL;
UPDATE edge_status SET total_memory = 0 WHERE total_memory IS NULL;

-- 2. 更改列为 NOT NULL
ALTER TABLE edge_status ALTER COLUMN cpu_usage SET NOT NULL;
ALTER TABLE edge_status ALTER COLUMN used_memory SET NOT NULL;
ALTER TABLE edge_status ALTER COLUMN total_memory SET NOT NULL;

-- 3. 设置默认值 (可选，但在数据库层面提供一致性)
ALTER TABLE edge_status ALTER COLUMN cpu_usage SET DEFAULT 0.0;
ALTER TABLE edge_status ALTER COLUMN used_memory SET DEFAULT 0;
ALTER TABLE edge_status ALTER COLUMN total_memory SET DEFAULT 0;
