-- V20260128.1340__add_remark_to_eop_transfer.sql
-- Add remark column to eop_transfer table

ALTER TABLE eop_transfer ADD COLUMN remark VARCHAR(256);
