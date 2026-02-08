-- V20260128.1600__remove_host_from_eop_bound.sql
-- 将 host 从 eop_bound 表中移除，改由 extra_params_json 存储

ALTER TABLE eop_bound DROP COLUMN host;
