-- V20260220.1900__add_edge_hardware_info.sql
-- 为 edge 表添加硬件和系统信息字段

-- 添加硬件信息字段
ALTER TABLE edge ADD COLUMN IF NOT EXISTS cpu_cores INTEGER;
ALTER TABLE edge ADD COLUMN IF NOT EXISTS cpu_model VARCHAR(256);
ALTER TABLE edge ADD COLUMN IF NOT EXISTS total_memory BIGINT;

-- 添加操作系统信息字段
ALTER TABLE edge ADD COLUMN IF NOT EXISTS os_type VARCHAR(64);
ALTER TABLE edge ADD COLUMN IF NOT EXISTS os_version VARCHAR(128);
ALTER TABLE edge ADD COLUMN IF NOT EXISTS os_arch VARCHAR(64);

-- 添加 Edge 程序版本信息
ALTER TABLE edge ADD COLUMN IF NOT EXISTS edge_version VARCHAR(64);

-- 添加字段注释
COMMENT ON COLUMN edge.cpu_cores IS 'CPU 核心数';
COMMENT ON COLUMN edge.cpu_model IS 'CPU 型号';
COMMENT ON COLUMN edge.total_memory IS '总内存大小（字节）';
COMMENT ON COLUMN edge.os_type IS '操作系统类型（Linux/Windows/MacOS等）';
COMMENT ON COLUMN edge.os_version IS '操作系统版本';
COMMENT ON COLUMN edge.os_arch IS '系统架构（x86_64/arm64等）';
COMMENT ON COLUMN edge.edge_version IS 'Edge 程序版本号';
