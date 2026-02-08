-- V20260130.1630__migrate_edge_nic_ip_columns.sql
-- 修改 edge_nic_ip 表的字段以匹配 Java 实体类更改

-- 1. 更新 status 字段
-- 首先将字符串值转换为对应的整数值，处理所有已知和未知的情况
UPDATE edge_nic_ip SET status = '1' WHERE status = 'ACTIVE';
UPDATE edge_nic_ip SET status = '0' WHERE status != '1' AND status != 'ACTIVE';

-- 在修改类型之前移除默认值，因为 PostgreSQL 无法自动将 'AVAILABLE' (String) 转换为 Integer
ALTER TABLE edge_nic_ip ALTER COLUMN status DROP DEFAULT;

-- 修改 status 列类型为 INTEGER，并设置默认值为 1 (ACTIVE)
ALTER TABLE edge_nic_ip ALTER COLUMN status TYPE INTEGER USING status::INTEGER;
ALTER TABLE edge_nic_ip ALTER COLUMN status SET DEFAULT 1;

-- 2. 设置 private_ip 为 NOT NULL
-- 确保没有空值或空字符串。如果存在，则设置为 '0.0.0.0'
UPDATE edge_nic_ip SET private_ip = '0.0.0.0' WHERE private_ip IS NULL OR private_ip = '';
ALTER TABLE edge_nic_ip ALTER COLUMN private_ip SET NOT NULL;

-- 3. 为 edge_nic_ip 增加级联删除约束（如果尚未在 V20260130.1600 中包含）
-- 检查并添加级联删除，以确保删除网卡时同步删除 IP
ALTER TABLE edge_nic_ip DROP CONSTRAINT IF EXISTS fk_edge_nic_ip_nic;
ALTER TABLE edge_nic_ip 
    ADD CONSTRAINT fk_edge_nic_ip_nic 
    FOREIGN KEY (nic_id) REFERENCES edge_nic(id) 
    ON DELETE CASCADE;
