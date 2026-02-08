-- V20260125.1120_edge__init_edge_management.sql
-- 边缘节点管理领域初始脚本

CREATE TABLE IF NOT EXISTS edge_area (
    id SERIAL PRIMARY KEY,
    parent_id INTEGER,
    level VARCHAR(32) NOT NULL,
    code VARCHAR(64) NOT NULL UNIQUE,
    name VARCHAR(128) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS edge (
    id SERIAL PRIMARY KEY,
    edge_tag VARCHAR(64) NOT NULL UNIQUE,
    name VARCHAR(128) NOT NULL,
    region_id INTEGER NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'OFFLINE',
    capabilities JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_edge_area FOREIGN KEY (region_id) REFERENCES edge_area(id)
);

CREATE TABLE IF NOT EXISTS edge_nic (
    id SERIAL PRIMARY KEY,
    edge_id INTEGER NOT NULL,
    mac_address VARCHAR(18) NOT NULL,
    nic_name VARCHAR(64),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_edge_nic_edge FOREIGN KEY (edge_id) REFERENCES edge(id)
);

CREATE TABLE IF NOT EXISTS edge_nic_ip (
    id SERIAL PRIMARY KEY,
    nic_id INTEGER NOT NULL,
    private_ip VARCHAR(64),
    public_ip VARCHAR(64),
    status VARCHAR(32) NOT NULL DEFAULT 'AVAILABLE',
    allocated_to_type VARCHAR(64),
    allocated_to_id VARCHAR(64),
    allocated_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    removed_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_edge_nic_ip_nic FOREIGN KEY (nic_id) REFERENCES edge_nic(id)
);

-- 边缘节点状态监控表（时序数据）
CREATE TABLE IF NOT EXISTS edge_status (
    edge_id INTEGER NOT NULL,
    sample_time TIMESTAMP WITH TIME ZONE NOT NULL,
    cpu_usage DOUBLE PRECISION,
    used_memory BIGINT,
    total_memory BIGINT,
    PRIMARY KEY (edge_id, sample_time), -- 复合索引优化时序查询
    CONSTRAINT fk_edge_status_edge FOREIGN KEY (edge_id) REFERENCES edge(id)
);
