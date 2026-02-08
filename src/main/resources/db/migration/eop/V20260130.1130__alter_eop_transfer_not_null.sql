-- V20260130.1130__alter_eop_transfer_not_null.sql
-- 将 eop_transfer 表中的 send_ip_id, receive_ip_id, host_name, protocol 字段设置为 NOT NULL

-- 如果这些字段中存在 NULL 值，可能需要先更新默认值或处理这些数据
-- 鉴于目前是开发阶段，可以直接执行 ALTER TABLE

ALTER TABLE eop_transfer ALTER COLUMN send_ip_id SET NOT NULL;
ALTER TABLE eop_transfer ALTER COLUMN receive_ip_id SET NOT NULL;
ALTER TABLE eop_transfer ALTER COLUMN host_name SET NOT NULL;
ALTER TABLE eop_transfer ALTER COLUMN protocol SET NOT NULL;
