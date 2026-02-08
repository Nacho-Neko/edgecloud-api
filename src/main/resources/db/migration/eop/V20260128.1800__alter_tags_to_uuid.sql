-- V20260128.1800__alter_tags_to_uuid.sql
-- 将各种 tag 和 owner_id 字段从 VARCHAR 更改为 UUID

-- Edge
ALTER TABLE edge ALTER COLUMN edge_tag TYPE UUID USING edge_tag::UUID;

-- EopApp
ALTER TABLE eop_app ALTER COLUMN eop_tag TYPE UUID USING eop_tag::UUID;

-- EopProduct
ALTER TABLE eop_product ALTER COLUMN tag TYPE UUID USING tag::UUID;

-- EopService
ALTER TABLE eop_service ALTER COLUMN tag TYPE UUID USING tag::UUID;
ALTER TABLE eop_service ALTER COLUMN owner_id TYPE UUID USING owner_id::UUID;

-- EopBound
ALTER TABLE eop_bound ALTER COLUMN tag TYPE UUID USING tag::UUID;
ALTER TABLE eop_bound ALTER COLUMN owner_id TYPE UUID USING owner_id::UUID;
