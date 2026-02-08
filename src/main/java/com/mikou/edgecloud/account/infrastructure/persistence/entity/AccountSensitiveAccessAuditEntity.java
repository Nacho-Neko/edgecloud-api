package com.mikou.edgecloud.account.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.Instant;
import java.util.UUID;

@TableName("account_sensitive_access_audit")
public class AccountSensitiveAccessAuditEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private UUID id;

    @TableField("admin_id")
    private UUID adminId;

    @TableField("kyc_id")
    private UUID kycId;

    @TableField("action")
    private String action;

    @TableField("created_at")
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public AccountSensitiveAccessAuditEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public AccountSensitiveAccessAuditEntity setAdminId(UUID adminId) {
        this.adminId = adminId;
        return this;
    }

    public UUID getKycId() {
        return kycId;
    }

    public AccountSensitiveAccessAuditEntity setKycId(UUID kycId) {
        this.kycId = kycId;
        return this;
    }

    public String getAction() {
        return action;
    }

    public AccountSensitiveAccessAuditEntity setAction(String action) {
        this.action = action;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AccountSensitiveAccessAuditEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
