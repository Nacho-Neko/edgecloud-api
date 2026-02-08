-- V20260131.1810__change_edge_nic_ip_allocated_to_id_to_uuid.sql
-- 将 edge_nic_ip 表的 allocated_to_id 字段修改为 UUID 类型

ALTER TABLE edge_nic_ip 
ALTER COLUMN allocated_to_id TYPE UUID USING allocated_to_id::UUID;
