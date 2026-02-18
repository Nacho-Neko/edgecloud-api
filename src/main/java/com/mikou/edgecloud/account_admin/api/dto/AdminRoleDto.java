package com.mikou.edgecloud.account_admin.api.dto;

import java.util.List;
import java.util.UUID;

public class AdminRoleDto {
    private UUID id;
    private String code;
    private String name;
    private UUID parentId;
    private String parentName;
    private String description;
    private String status;
    private Integer sortOrder;
    private List<String> permissionCodes;

    public UUID getId() {
        return id;
    }

    public AdminRoleDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public AdminRoleDto setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public AdminRoleDto setName(String name) {
        this.name = name;
        return this;
    }

    public UUID getParentId() {
        return parentId;
    }

    public AdminRoleDto setParentId(UUID parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getParentName() {
        return parentName;
    }

    public AdminRoleDto setParentName(String parentName) {
        this.parentName = parentName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AdminRoleDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public AdminRoleDto setStatus(String status) {
        this.status = status;
        return this;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public AdminRoleDto setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public List<String> getPermissionCodes() {
        return permissionCodes;
    }

    public AdminRoleDto setPermissionCodes(List<String> permissionCodes) {
        this.permissionCodes = permissionCodes;
        return this;
    }
}
