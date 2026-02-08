-- V20260128.1135__add_pair_key_to_eop_transfer.sql
-- 在 eop_transfer 表中添加 pair_key 字段

ALTER TABLE eop_transfer ADD COLUMN pair_key UUID NOT NULL DEFAULT gen_random_uuid();
CREATE INDEX idx_eop_transfer_pair_key ON eop_transfer(pair_key);
