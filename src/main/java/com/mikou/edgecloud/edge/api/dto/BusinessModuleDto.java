package com.mikou.edgecloud.edge.api.dto;

/**
 * 业务模块信息 DTO
 */
public class BusinessModuleDto {
    private String code;          // 模块代码，如：EOP
    private String name;          // 模块名称，如：边缘运营平台
    private String description;   // 模块描述
    private boolean enabled;      // 是否启用
    private String version;       // 模块版本

    public String getCode() {
        return code;
    }

    public BusinessModuleDto setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public BusinessModuleDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BusinessModuleDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public BusinessModuleDto setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public BusinessModuleDto setVersion(String version) {
        this.version = version;
        return this;
    }
}
