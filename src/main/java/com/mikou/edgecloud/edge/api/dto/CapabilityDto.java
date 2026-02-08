package com.mikou.edgecloud.edge.api.dto;

import java.util.Map;

/**
 * 能力详情 DTO
 */
public class CapabilityDto {
    private String name;           // 能力名称，如 "CDN"
    private boolean enabled;       // 是否启用
    private Map<String, Object> config;  // 能力配置（可选）

    public CapabilityDto() {}

    public CapabilityDto(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public String getName() { return name; }
    public CapabilityDto setName(String name) { this.name = name; return this; }

    public boolean isEnabled() { return enabled; }
    public CapabilityDto setEnabled(boolean enabled) { this.enabled = enabled; return this; }

    public Map<String, Object> getConfig() { return config; }
    public CapabilityDto setConfig(Map<String, Object> config) { this.config = config; return this; }
}