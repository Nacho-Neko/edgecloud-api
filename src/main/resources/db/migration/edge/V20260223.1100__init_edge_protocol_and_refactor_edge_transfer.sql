-- V20260223.1100__init_edge_protocol_and_refactor_edge_transfer.sql
-- 1. 创建 edge_protocol 表（Edge 层监听器配置，从 EOP 层 settings.listeners 迁移而来）
CREATE TABLE IF NOT EXISTS edge_protocol (
    id          SERIAL PRIMARY KEY,
    edge_tag    UUID         NOT NULL,
    protocol    VARCHAR(64)  NOT NULL,
    ip_id       INTEGER      NOT NULL,
    port        INTEGER,
    port_range_start INTEGER,
    port_range_end   INTEGER,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    removed_at  TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_edge_protocol_ip FOREIGN KEY (ip_id) REFERENCES edge_nic_ip(id)
);

-- 2. 将 eop_transfer 重命名为 edge_transfer
ALTER TABLE eop_transfer RENAME TO edge_transfer;

-- 3. 新增 target_protocol_id：指向 edge_protocol 记录（传输目标端监听器）
ALTER TABLE edge_transfer ADD COLUMN target_protocol_id INTEGER;
ALTER TABLE edge_transfer ADD CONSTRAINT fk_edge_transfer_protocol
    FOREIGN KEY (target_protocol_id) REFERENCES edge_protocol(id);

-- 4. 移除已由 edge_protocol 承载的冗余列
ALTER TABLE edge_transfer DROP COLUMN IF EXISTS receive_ip_id;
ALTER TABLE edge_transfer DROP COLUMN IF EXISTS protocol;
