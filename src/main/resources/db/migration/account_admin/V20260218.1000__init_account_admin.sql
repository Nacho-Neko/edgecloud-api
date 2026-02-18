-- V20260218.1000__init_account_admin.sql
-- 管理员账户领域初始化脚本

-- 1. 管理员角色表（支持父子继承）
CREATE TABLE IF NOT EXISTS admin_role (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(64) NOT NULL UNIQUE,
    name VARCHAR(128) NOT NULL,
    parent_id UUID,
    description TEXT,
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    CONSTRAINT fk_admin_role_parent FOREIGN KEY (parent_id) REFERENCES admin_role(id) ON DELETE SET NULL
);

-- 2. 管理员权限表
CREATE TABLE IF NOT EXISTS admin_permission (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(64) NOT NULL UNIQUE,
    name VARCHAR(128) NOT NULL,
    resource_type VARCHAR(64),
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. 角色权限关联表
CREATE TABLE IF NOT EXISTS admin_role_permission (
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES admin_role(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES admin_permission(id) ON DELETE CASCADE
);

-- 4. 管理员账户表
CREATE TABLE IF NOT EXISTS account_admin (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(64) NOT NULL UNIQUE,
    email VARCHAR(128) NOT NULL UNIQUE,
    password_hash VARCHAR(256) NOT NULL,
    role_id UUID NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
    last_login_at TIMESTAMP WITH TIME ZONE,
    last_login_ip VARCHAR(64),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    CONSTRAINT fk_account_admin_role FOREIGN KEY (role_id) REFERENCES admin_role(id)
);

-- 5. 管理员操作审计日志表
CREATE TABLE IF NOT EXISTS account_admin_audit (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    admin_id UUID NOT NULL,
    action VARCHAR(128) NOT NULL,
    resource_type VARCHAR(64),
    resource_id VARCHAR(128),
    ip_address VARCHAR(64),
    user_agent TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_admin_audit_admin FOREIGN KEY (admin_id) REFERENCES account_admin(id)
);

-- 创建索引
CREATE INDEX idx_admin_role_code ON admin_role(code);
CREATE INDEX idx_admin_role_status ON admin_role(status);
CREATE INDEX idx_admin_role_parent_id ON admin_role(parent_id);
CREATE INDEX idx_admin_permission_code ON admin_permission(code);
CREATE INDEX idx_admin_permission_resource_type ON admin_permission(resource_type);
CREATE INDEX idx_account_admin_username ON account_admin(username);
CREATE INDEX idx_account_admin_email ON account_admin(email);
CREATE INDEX idx_account_admin_role_id ON account_admin(role_id);
CREATE INDEX idx_account_admin_status ON account_admin(status);
CREATE INDEX idx_admin_audit_admin_id ON account_admin_audit(admin_id);
CREATE INDEX idx_admin_audit_created_at ON account_admin_audit(created_at);

-- ========== 初始化权限数据 ==========

-- 系统管理权限
INSERT INTO admin_permission (code, name, resource_type, description) VALUES
('ADMIN_ALL', '全部权限', 'SYSTEM', '系统管理员拥有的所有权限'),
('ADMIN_MANAGE', '管理员管理', 'ADMIN', '管理管理员账户'),
('ROLE_MANAGE', '角色管理', 'ADMIN', '管理角色和权限');

-- 财务模块权限
INSERT INTO admin_permission (code, name, resource_type, description) VALUES
('FINANCE_MANAGE', '财务管理', 'FINANCE', '管理财务数据和配置'),
('FINANCE_VIEW', '财务查看', 'FINANCE', '查看财务数据');

-- 客服模块权限
INSERT INTO admin_permission (code, name, resource_type, description) VALUES
('CS_MANAGE', '客服管理', 'CUSTOMER_SERVICE', '管理客服功能和配置'),
('CS_VIEW', '客服查看', 'CUSTOMER_SERVICE', '查看客服数据');

-- KYC 权限
INSERT INTO admin_permission (code, name, resource_type, description) VALUES
('KYC_REVEAL', 'KYC解密', 'KYC', '解密查看实名信息'),
('KYC_VIEW', 'KYC查看', 'KYC', '查看脱敏的实名信息');

-- Edge 节点管理权限
INSERT INTO admin_permission (code, name, resource_type, description) VALUES
('EDGE_MANAGE', '节点管理', 'EDGE', '管理边缘节点'),
('EDGE_VIEW', '节点查看', 'EDGE', '查看边缘节点信息');

-- EOP 平台管理权限
INSERT INTO admin_permission (code, name, resource_type, description) VALUES
('EOP_MANAGE', 'EOP管理', 'EOP', '管理EOP平台配置'),
('EOP_VIEW', 'EOP查看', 'EOP', '查看EOP平台数据');

-- ========== 初始化角色数据（带继承关系） ==========

-- 1. 系统管理员（顶级角色，没有父角色）
INSERT INTO admin_role (code, name, description, sort_order, parent_id) VALUES
('SYSTEM_ADMIN', '系统管理员', '拥有所有权限，可管理系统所有功能', 1, NULL);

-- 2. 财务管理员（继承系统管理员的所有权限，然后再添加自己的）
INSERT INTO admin_role (code, name, description, sort_order, parent_id) VALUES
('FINANCE_ADMIN', '财务管理员', '拥有财务模块的管理和查看权限', 2, NULL);

-- 3. 财务（继承财务管理员的权限）
INSERT INTO admin_role (code, name, description, sort_order, parent_id)
SELECT 'FINANCE', '财务', '拥有财务模块的查看权限', 3, id
FROM admin_role WHERE code = 'FINANCE_ADMIN';

-- 4. 客服管理员（独立角色）
INSERT INTO admin_role (code, name, description, sort_order, parent_id) VALUES
('CUSTOMER_SERVICE_ADMIN', '客服管理员', '拥有客服模块管理权限和KYC查看/解密权限', 4, NULL);

-- 5. 客服（继承客服管理员的权限）
INSERT INTO admin_role (code, name, description, sort_order, parent_id)
SELECT 'CUSTOMER_SERVICE', '客服', '拥有客服模块查看权限和KYC脱敏查看权限', 5, id
FROM admin_role WHERE code = 'CUSTOMER_SERVICE_ADMIN';

-- ========== 配置角色权限关联 ==========
-- 注意：子角色会自动继承父角色的所有权限，这里只需配置角色直接拥有的权限

-- 系统管理员 - 拥有所有权限（顶级角色）
INSERT INTO admin_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM admin_role r, admin_permission p
WHERE r.code = 'SYSTEM_ADMIN';

-- 财务管理员 - 财务管理+查看（基础权限）
INSERT INTO admin_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM admin_role r, admin_permission p
WHERE r.code = 'FINANCE_ADMIN' AND p.code IN ('FINANCE_MANAGE', 'FINANCE_VIEW');

-- 财务 - 只有财务查看权限（但会继承财务管理员的其他权限，如果有父角色）
-- 当前财务角色继承财务管理员，所以实际拥有：FINANCE_MANAGE, FINANCE_VIEW
-- 这里不额外添加权限，完全依靠继承

-- 客服管理员 - 客服管理+查看 + KYC解密+查看（基础权限）
INSERT INTO admin_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM admin_role r, admin_permission p
WHERE r.code = 'CUSTOMER_SERVICE_ADMIN' AND p.code IN ('CS_MANAGE', 'CS_VIEW', 'KYC_REVEAL', 'KYC_VIEW');

-- 客服 - 只有客服查看 + KYC查看（直接权限）
-- 但会继承客服管理员的所有权限：CS_MANAGE, CS_VIEW, KYC_REVEAL, KYC_VIEW
INSERT INTO admin_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM admin_role r, admin_permission p
WHERE r.code = 'CUSTOMER_SERVICE' AND p.code IN ('CS_VIEW', 'KYC_VIEW');

-- ========== 创建默认系统管理员账户 ==========

-- 插入默认系统管理员（密码：Admin@123，需要首次登录后修改）
-- BCrypt hash for "Admin@123"
INSERT INTO account_admin (username, email, password_hash, role_id, status)
SELECT 'admin', 'admin@edgecloud.internal', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', id, 'ACTIVE'
FROM admin_role WHERE code = 'SYSTEM_ADMIN'
ON CONFLICT (username) DO NOTHING;

-- 表注释
COMMENT ON TABLE admin_role IS '管理员角色表';
COMMENT ON TABLE admin_permission IS '管理员权限表';
COMMENT ON TABLE admin_role_permission IS '角色权限关联表';
COMMENT ON TABLE account_admin IS '管理员账户表';
COMMENT ON TABLE account_admin_audit IS '管理员操作审计日志';

-- 列注释
COMMENT ON COLUMN admin_role.code IS '角色代码，唯一标识';
COMMENT ON COLUMN admin_role.name IS '角色显示名称';
COMMENT ON COLUMN admin_role.parent_id IS '父角色ID，子角色自动继承父角色的所有权限';
COMMENT ON COLUMN admin_role.sort_order IS '排序顺序，数字越小越靠前';
COMMENT ON COLUMN admin_permission.code IS '权限代码，对应Spring Security的Authority';
COMMENT ON COLUMN admin_permission.resource_type IS '资源类型，用于权限分组';
COMMENT ON COLUMN account_admin.role_id IS '关联的角色ID';

