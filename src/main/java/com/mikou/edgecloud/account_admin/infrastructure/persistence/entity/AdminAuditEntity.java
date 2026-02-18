package com.mikou.edgecloud.account_admin.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.Instant;
import java.util.UUID;

@TableName("account_admin_audit")
public class AdminAuditEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private UUID id;

    @TableField("admin_id")
    private UUID adminId;

    @TableField("action")
    private String action;

    @TableField("resource_type")
    private String resourceType;

    @TableField("resource_id")
    private String resourceId;

    @TableField("ip_address")
    private String ipAddress;

    @TableField("user_agent")
    private String userAgent;

    @TableField("created_at")
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public AdminAuditEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public AdminAuditEntity setAdminId(UUID adminId) {
        this.adminId = adminId;
        return this;
    }

    public String getAction() {
        return action;
    }

    public AdminAuditEntity setAction(String action) {
        this.action = action;
        return this;
    }

    public String getResourceType() {
        return resourceType;
    }

    public AdminAuditEntity setResourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public String getResourceId() {
        return resourceId;
    }

    public AdminAuditEntity setResourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public AdminAuditEntity setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public AdminAuditEntity setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AdminAuditEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
