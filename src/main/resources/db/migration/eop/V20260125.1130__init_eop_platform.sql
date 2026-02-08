-- V20260125.1130_eop__init_eop_platform.sql
-- 业务开放平台领域初始脚本

CREATE TABLE IF NOT EXISTS eop_product (
    id SERIAL PRIMARY KEY,
    tag VARCHAR(64) NOT NULL UNIQUE,
    name VARCHAR(128) NOT NULL,
    monthly_price DECIMAL(19, 4),
    max_outbound_count INTEGER,
    max_inbound_count INTEGER,
    outbound_max_connections INTEGER,
    inbound_max_connections INTEGER,
    allowed_entry_level INTEGER,
    max_origin_targets INTEGER,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    removed_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS eop_service (
    id SERIAL PRIMARY KEY,
    tag VARCHAR(64) NOT NULL UNIQUE,
    owner_id VARCHAR(64) NOT NULL,
    product_id INTEGER NOT NULL,
    monthly_price DECIMAL(19, 4),
    entitlements_json TEXT,
    status VARCHAR(32) NOT NULL DEFAULT 'INACTIVE',
    outbound_work_id INTEGER,
    expired_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    removed_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_eop_service_product FOREIGN KEY (product_id) REFERENCES eop_product(id)
);

CREATE TABLE IF NOT EXISTS eop_app (
    id SERIAL PRIMARY KEY,
    eop_tag VARCHAR(64) NOT NULL UNIQUE,
    edge_id INTEGER NOT NULL,
    settings JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS eop_bound (
    id SERIAL PRIMARY KEY,
    tag VARCHAR(64) NOT NULL UNIQUE,
    service_id INTEGER NOT NULL,
    work_id INTEGER NOT NULL,
    owner_id VARCHAR(64) NOT NULL,
    product_id INTEGER NOT NULL,
    direction VARCHAR(32) NOT NULL,
    addr_id INTEGER,
    max_connections INTEGER,
    port INTEGER,
    outbound_tag VARCHAR(64),
    host_json TEXT,
    transfer_route_json TEXT,
    extra_params_json TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    removed_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_eop_bound_service FOREIGN KEY (service_id) REFERENCES eop_service(id),
    CONSTRAINT fk_eop_bound_product FOREIGN KEY (product_id) REFERENCES eop_product(id)
);

CREATE TABLE IF NOT EXISTS eop_port_occupation (
    id SERIAL PRIMARY KEY,
    ip_id INTEGER NOT NULL,
    port INTEGER NOT NULL,
    occupier_type VARCHAR(64) NOT NULL,
    occupier_id INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (ip_id, port)
);

CREATE TABLE IF NOT EXISTS eop_transfer (
    id SERIAL PRIMARY KEY,
    sender_eop INTEGER NOT NULL,
    target_eop INTEGER NOT NULL,
    send_ip_id INTEGER,
    receive_ip_id INTEGER,
    host_name VARCHAR(128),
    protocol VARCHAR(32),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    removed_at TIMESTAMP WITH TIME ZONE
);
