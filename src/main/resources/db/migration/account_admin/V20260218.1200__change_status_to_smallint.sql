-- V20260218.1200__change_status_to_smallint.sql
-- 将 status 字段从 VARCHAR 改为 SMALLINT 以节省存储空间和提高性能

-- 1. 删除原有的字符串约束
ALTER TABLE admin_role DROP CONSTRAINT IF EXISTS chk_role_status;
ALTER TABLE account_admin DROP CONSTRAINT IF EXISTS chk_admin_status;

-- 2. 修改 admin_role 表的 status 字段
-- 先将现有数据转换为数值字符串
UPDATE admin_role SET status = 
    CASE 
        WHEN status = 'ACTIVE' THEN '1'
        WHEN status = 'DISABLED' THEN '2'
        WHEN status = 'DELETED' THEN '3'
        ELSE '1'
    END;

-- 删除默认值（避免类型转换冲突）
ALTER TABLE admin_role ALTER COLUMN status DROP DEFAULT;

-- 修改字段类型
ALTER TABLE admin_role ALTER COLUMN status TYPE SMALLINT USING status::SMALLINT;

-- 设置新的数值默认值
ALTER TABLE admin_role ALTER COLUMN status SET DEFAULT 1;

-- 添加新的数值约束
ALTER TABLE admin_role ADD CONSTRAINT chk_role_status CHECK (status IN (1, 2, 3));

-- 3. 修改 account_admin 表的 status 字段
-- 先将现有数据转换为数值字符串
UPDATE account_admin SET status = 
    CASE 
        WHEN status = 'ACTIVE' THEN '1'
        WHEN status = 'DISABLED' THEN '2'
        WHEN status = 'DELETED' THEN '3'
        ELSE '1'
    END;

-- 删除默认值（避免类型转换冲突）
ALTER TABLE account_admin ALTER COLUMN status DROP DEFAULT;

-- 修改字段类型
ALTER TABLE account_admin ALTER COLUMN status TYPE SMALLINT USING status::SMALLINT;

-- 设置新的数值默认值
ALTER TABLE account_admin ALTER COLUMN status SET DEFAULT 1;

-- 添加新的数值约束
ALTER TABLE account_admin ADD CONSTRAINT chk_admin_status CHECK (status IN (1, 2, 3));

-- 4. 更新列注释
COMMENT ON COLUMN admin_role.status IS '角色状态枚举: 1=ACTIVE(激活), 2=DISABLED(禁用), 3=DELETED(已删除)';
COMMENT ON COLUMN account_admin.status IS '管理员状态枚举: 1=ACTIVE(激活), 2=DISABLED(禁用), 3=DELETED(已删除)';
