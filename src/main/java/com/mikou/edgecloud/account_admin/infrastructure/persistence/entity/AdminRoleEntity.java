package com.mikou.edgecloud.account_admin.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mikou.edgecloud.account_admin.domain.RoleStatus;
import java.time.Instant;
import java.util.UUID;

@TableName("admin_role")
public class AdminRoleEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private UUID id;

    @TableField("code")
    private String code;

    @TableField("name")
    private String name;

    @TableField("parent_id")
    private UUID parentId;

    @TableField("description")
    private String description;

    @TableField("status")
    private RoleStatus status;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("created_at")
    private Instant createdAt;

    @TableField("updated_at")
    private Instant updatedAt;

    @TableField("created_by")
    private UUID createdBy;

    public UUID getId() {
        return id;
    }

    public AdminRoleEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public AdminRoleEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public AdminRoleEntity setName(String name) {
        this.name = name;
        return this;
    }

    public UUID getParentId() {
        return parentId;
    }

    public AdminRoleEntity setParentId(UUID parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AdminRoleEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public RoleStatus getStatus() {
        return status;
    }

    public AdminRoleEntity setStatus(RoleStatus status) {
        this.status = status;
        return this;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public AdminRoleEntity setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AdminRoleEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public AdminRoleEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public AdminRoleEntity setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
        return this;
    }
}
