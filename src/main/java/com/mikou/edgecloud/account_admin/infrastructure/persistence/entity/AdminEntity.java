package com.mikou.edgecloud.account_admin.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mikou.edgecloud.account.domain.AccountStatus;
import java.time.Instant;
import java.util.UUID;

@TableName("account_admin")
public class AdminEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private UUID id;

    @TableField("username")
    private String username;

    @TableField("email")
    private String email;

    @TableField("password_hash")
    private String passwordHash;

    @TableField("role_id")
    private UUID roleId;

    @TableField("account_status")
    private AccountStatus accountStatus;

    @TableField("last_login_at")
    private Instant lastLoginAt;

    @TableField("last_login_ip")
    private String lastLoginIp;

    @TableField("created_at")
    private Instant createdAt;

    @TableField("updated_at")
    private Instant updatedAt;

    @TableField("created_by")
    private UUID createdBy;

    public UUID getId() {
        return id;
    }

    public AdminEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AdminEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AdminEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public AdminEntity setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public AdminEntity setRoleId(UUID roleId) {
        this.roleId = roleId;
        return this;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public AdminEntity setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
        return this;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public AdminEntity setLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
        return this;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public AdminEntity setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AdminEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public AdminEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public AdminEntity setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
        return this;
    }
}
