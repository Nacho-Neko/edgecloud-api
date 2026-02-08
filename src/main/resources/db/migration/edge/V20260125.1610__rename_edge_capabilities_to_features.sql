-- V20260125.1610__rename_edge_capabilities_to_features.sql
-- 将 edge 表中的 capabilities 字段重命名为 features

ALTER TABLE edge RENAME COLUMN capabilities TO features;
