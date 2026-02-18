package com.mikou.edgecloud.account_admin.api.dto;

import java.util.UUID;

public class AdminPermissionDto {
    private UUID id;
    private String code;
    private String name;
    private String resourceType;
    private String description;

    public UUID getId() {
        return id;
    }

    public AdminPermissionDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public AdminPermissionDto setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public AdminPermissionDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getResourceType() {
        return resourceType;
    }

    public AdminPermissionDto setResourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AdminPermissionDto setDescription(String description) {
        this.description = description;
        return this;
    }
}
