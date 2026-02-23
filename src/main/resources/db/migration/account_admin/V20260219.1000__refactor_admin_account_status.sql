-- V20260219.1000__refactor_admin_account_status.sql
-- 重构管理员账户状态字段，使用更明确的命名避免歧义

-- 1. 修改 account_admin 表的 status 字段名为 account_status
ALTER TABLE account_admin RENAME COLUMN status TO account_status;

-- 2. 更新索引名称以匹配新字段名
DROP INDEX IF EXISTS idx_account_admin_status;
CREATE INDEX idx_account_admin_account_status ON account_admin(account_status);

-- 3. 更新默认值约束
-- 注意：账户状态有 ENABLED(1)、DISABLED(2)、DELETED(3) 三个状态，默认为 ENABLED
-- 删除只能标记为 DELETED，不能真正删除记录
ALTER TABLE account_admin ALTER COLUMN account_status SET DEFAULT '1';

COMMENT ON COLUMN account_admin.account_status IS '账户状态：1-启用(ENABLED)，2-停用(DISABLED)，3-已删除(DELETED，软删除)';
COMMENT ON COLUMN admin_role.status IS '角色状态：1-启用(ENABLED)，2-停用(DISABLED)，3-已删除(DELETED，软删除)';
