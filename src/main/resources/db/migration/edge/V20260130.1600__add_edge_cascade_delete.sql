-- V20260130.1600__add_edge_cascade_delete.sql
-- 为 edge 关联表增加级联删除约束

-- 1. edge_status 表：删除 edge 时同步删除监控状态
ALTER TABLE edge_status DROP CONSTRAINT IF EXISTS fk_edge_status_edge;
ALTER TABLE edge_status 
    ADD CONSTRAINT fk_edge_status_edge 
    FOREIGN KEY (edge_id) REFERENCES edge(id) 
    ON DELETE CASCADE;

-- 2. edge_nic 表：删除 edge 时同步删除网卡信息
ALTER TABLE edge_nic DROP CONSTRAINT IF EXISTS fk_edge_nic_edge;
ALTER TABLE edge_nic 
    ADD CONSTRAINT fk_edge_nic_edge 
    FOREIGN KEY (edge_id) REFERENCES edge(id) 
    ON DELETE CASCADE;

-- 3. eop_app 表：删除 edge 时同步删除 EOP 应用
-- 先检查并添加可能缺失的外键约束，并设置级联删除
ALTER TABLE eop_app DROP CONSTRAINT IF EXISTS fk_eop_app_edge;
ALTER TABLE eop_app 
    ADD CONSTRAINT fk_eop_app_edge 
    FOREIGN KEY (edge_id) REFERENCES edge(id) 
    ON DELETE CASCADE;
