package com.mikou.edgecloud.edge.api.dto;

import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
public class CapabilityRequest {
    private String name;           // 能力名称，如 "CDN"
    private boolean enabled;       // 是否启用

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
