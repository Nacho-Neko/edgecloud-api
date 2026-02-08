package com.mikou.edgecloud.account.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.Instant;
import java.util.UUID;

@TableName("account")
public class AccountEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private UUID id;

    @TableField("username")
    private String username;

    @TableField("email")
    private String email;

    @TableField("password_hash")
    private String passwordHash;

    @TableField("status")
    private String status;

    @TableField("created_at")
    private Instant createdAt;

    @TableField("updated_at")
    private Instant updatedAt;

    public UUID getId() {
        return id;
    }

    public AccountEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AccountEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AccountEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public AccountEntity setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public AccountEntity setStatus(String status) {
        this.status = status;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AccountEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public AccountEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
