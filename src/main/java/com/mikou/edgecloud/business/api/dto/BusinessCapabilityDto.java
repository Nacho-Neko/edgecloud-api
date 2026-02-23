package com.mikou.edgecloud.business.api.dto;

/**
 * 业务能力信息 DTO
 */
public class BusinessCapabilityDto {
    private String code;          // 能力代码，如：EOP
    private String name;          // 能力名称，如：边缘运营平台
    private String description;   // 能力描述
    private boolean enabled;      // 是否启用
    private String version;       // 版本号

    public String getCode() {
        return code;
    }

    public BusinessCapabilityDto setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public BusinessCapabilityDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BusinessCapabilityDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public BusinessCapabilityDto setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public BusinessCapabilityDto setVersion(String version) {
        this.version = version;
        return this;
    }
}
