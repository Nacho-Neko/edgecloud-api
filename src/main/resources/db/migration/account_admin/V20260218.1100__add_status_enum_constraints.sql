-- V20260218.1100__add_status_enum_constraints.sql
-- 为 status 字段添加枚举约束，配合 Java 枚举使用

-- 1. 为 admin_role 表添加 status CHECK 约束
ALTER TABLE admin_role 
ADD CONSTRAINT chk_role_status CHECK (status IN ('ACTIVE', 'DISABLED', 'DELETED'));

-- 2. 为 account_admin 表添加 status CHECK 约束
ALTER TABLE account_admin 
ADD CONSTRAINT chk_admin_status CHECK (status IN ('ACTIVE', 'DISABLED', 'DELETED'));

-- 3. 添加列注释说明枚举值
COMMENT ON COLUMN admin_role.status IS '角色状态枚举: ACTIVE=激活, DISABLED=禁用, DELETED=已删除';
COMMENT ON COLUMN account_admin.status IS '管理员状态枚举: ACTIVE=激活, DISABLED=禁用, DELETED=已删除';
