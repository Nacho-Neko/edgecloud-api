-- V20260223.1000__refactor_transfer_sender_target_to_edge_tag.sql
-- 传输路径从 EOP 层上移到 Edge 层，发送/接收端标识由 EOP Tag 改为 Edge Tag

ALTER TABLE eop_transfer RENAME COLUMN sender_eop TO sender_edge_tag;
ALTER TABLE eop_transfer RENAME COLUMN target_eop TO target_edge_tag;
