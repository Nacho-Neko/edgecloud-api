-- V20260202.1400__rename_eop_bound_work_id_to_eop_id.sql
-- 将 eop_bound 表中的 work_id 字段重命名为 eop_id，以更符合业务语义

ALTER TABLE eop_bound RENAME COLUMN work_id TO eop_id;
