-- V20260128.1400__alter_sender_target_eop_to_uuid.sql
-- 将 eop_transfer 表中的 sender_eop 和 target_eop 字段改为 UUID 类型

-- 1. 修改 sender_eop 类型
ALTER TABLE eop_transfer 
ALTER COLUMN sender_eop TYPE UUID USING (
    CASE 
        WHEN sender_eop::text ~ '^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$' 
        THEN sender_eop::text::UUID 
        ELSE NULL 
    END
);

-- 2. 修改 target_eop 类型
ALTER TABLE eop_transfer 
ALTER COLUMN target_eop TYPE UUID USING (
    CASE 
        WHEN target_eop::text ~ '^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$' 
        THEN target_eop::text::UUID 
        ELSE NULL 
    END
);
