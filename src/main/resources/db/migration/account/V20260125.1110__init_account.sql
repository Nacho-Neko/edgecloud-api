-- V20260125.1110_acc__init_account.sql
-- 账户与实名认证领域初始脚本

CREATE TABLE IF NOT EXISTS account (
    id VARCHAR(64) PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    email VARCHAR(128) NOT NULL UNIQUE,
    password_hash VARCHAR(256) NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS account_kyc (
    id BIGSERIAL PRIMARY KEY,
    account_id VARCHAR(64) NOT NULL,
    provider VARCHAR(64) NOT NULL,
    kyc_type VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL,
    external_request_id VARCHAR(128),
    external_reference_id VARCHAR(128),
    submitted_at TIMESTAMP WITH TIME ZONE,
    verified_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_kyc_account FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE IF NOT EXISTS account_kyc_person (
    kyc_id BIGINT PRIMARY KEY,
    real_name_plain VARCHAR(128),
    id_number_plain VARCHAR(128),
    id_number_masked VARCHAR(128),
    CONSTRAINT fk_account_kyc_person_kyc FOREIGN KEY (kyc_id) REFERENCES account_kyc(id)
);

CREATE TABLE IF NOT EXISTS account_sensitive_access_audit (
    id BIGSERIAL PRIMARY KEY,
    admin_id VARCHAR(64) NOT NULL,
    kyc_id BIGINT NOT NULL,
    action VARCHAR(64) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_admin FOREIGN KEY (admin_id) REFERENCES account(id),
    CONSTRAINT fk_audit_kyc FOREIGN KEY (kyc_id) REFERENCES account_kyc(id)
);
