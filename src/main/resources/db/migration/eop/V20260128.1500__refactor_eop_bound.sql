-- V20260128.1500__refactor_eop_bound.sql
-- 重构 eop_bound 表

-- 1. 移除 port 和 transfer_route_json
ALTER TABLE eop_bound DROP COLUMN IF EXISTS port;
ALTER TABLE eop_bound DROP COLUMN IF EXISTS transfer_route_json;

-- 2. 重命名 host_json 为 host (String 不可空)
ALTER TABLE eop_bound RENAME COLUMN host_json TO host;
-- 注意：如果已有数据，可能需要先处理空值。此处假设为新功能或可接受默认空字符串
UPDATE eop_bound SET host = '' WHERE host IS NULL;
ALTER TABLE eop_bound ALTER COLUMN host SET NOT NULL;

-- 3. 重命名 outbound_tag 为 out_tag 并改为 UUID
-- 先尝试转换现有数据
ALTER TABLE eop_bound RENAME COLUMN outbound_tag TO out_tag;
ALTER TABLE eop_bound ALTER COLUMN out_tag TYPE UUID USING (out_tag::uuid);

-- 4. addr_id 设置为不可空
UPDATE eop_bound SET addr_id = 0 WHERE addr_id IS NULL; -- 兜底
ALTER TABLE eop_bound ALTER COLUMN addr_id SET NOT NULL;
