-- V20260129.1400__refactor_account_ids_to_uuid.sql

-- 1. 处理 account_sensitive_access_audit 表 (删除外键以便修改主表)
ALTER TABLE account_sensitive_access_audit DROP CONSTRAINT IF EXISTS fk_audit_admin;
ALTER TABLE account_sensitive_access_audit DROP CONSTRAINT IF EXISTS fk_audit_kyc;

-- 2. 处理 account_kyc_person 表
ALTER TABLE account_kyc_person DROP CONSTRAINT IF EXISTS fk_account_kyc_person_kyc;

-- 3. 处理 account_kyc 表
ALTER TABLE account_kyc DROP CONSTRAINT IF EXISTS fk_account_kyc_account;

-- 4. 修改 account 表 id 为 UUID
ALTER TABLE account ALTER COLUMN id TYPE UUID USING (
    CASE 
        WHEN id ~ '^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$' 
        THEN id::UUID 
        ELSE gen_random_uuid() 
    END
);
ALTER TABLE account ALTER COLUMN id SET DEFAULT gen_random_uuid();
ALTER TABLE account ALTER COLUMN created_at SET NOT NULL;
ALTER TABLE account ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

-- 5. 修改 account_kyc 表
-- 修改 id
ALTER TABLE account_kyc ALTER COLUMN id DROP DEFAULT;
ALTER TABLE account_kyc ALTER COLUMN id TYPE UUID USING gen_random_uuid();
ALTER TABLE account_kyc ALTER COLUMN id SET DEFAULT gen_random_uuid();
-- 修改 account_id
ALTER TABLE account_kyc ALTER COLUMN account_id TYPE UUID USING account_id::UUID;
-- 修改 created_at
ALTER TABLE account_kyc ALTER COLUMN created_at SET NOT NULL;
ALTER TABLE account_kyc ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

-- 6. 修改 account_kyc_person 表
ALTER TABLE account_kyc_person ALTER COLUMN kyc_id TYPE UUID USING kyc_id::text::UUID;

-- 7. 修改 account_sensitive_access_audit 表
ALTER TABLE account_sensitive_access_audit ALTER COLUMN id DROP DEFAULT;
ALTER TABLE account_sensitive_access_audit ALTER COLUMN id TYPE UUID USING gen_random_uuid();
ALTER TABLE account_sensitive_access_audit ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE account_sensitive_access_audit ALTER COLUMN admin_id TYPE UUID USING admin_id::UUID;
ALTER TABLE account_sensitive_access_audit ALTER COLUMN kyc_id TYPE UUID USING kyc_id::text::UUID;
ALTER TABLE account_sensitive_access_audit ALTER COLUMN created_at SET NOT NULL;
ALTER TABLE account_sensitive_access_audit ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

-- 8. 恢复外键约束
ALTER TABLE account_kyc ADD CONSTRAINT fk_account_kyc_account FOREIGN KEY (account_id) REFERENCES account(id);
ALTER TABLE account_kyc_person ADD CONSTRAINT fk_account_kyc_person_kyc FOREIGN KEY (kyc_id) REFERENCES account_kyc(id);
ALTER TABLE account_sensitive_access_audit ADD CONSTRAINT fk_audit_admin FOREIGN KEY (admin_id) REFERENCES account(id);
ALTER TABLE account_sensitive_access_audit ADD CONSTRAINT fk_audit_kyc FOREIGN KEY (kyc_id) REFERENCES account_kyc(id);
