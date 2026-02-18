package com.mikou.edgecloud.account_admin.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.Instant;
import java.util.UUID;

@TableName("admin_permission")
public class AdminPermissionEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private UUID id;

    @TableField("code")
    private String code;

    @TableField("name")
    private String name;

    @TableField("resource_type")
    private String resourceType;

    @TableField("description")
    private String description;

    @TableField("created_at")
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public AdminPermissionEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public AdminPermissionEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public AdminPermissionEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getResourceType() {
        return resourceType;
    }

    public AdminPermissionEntity setResourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AdminPermissionEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AdminPermissionEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
