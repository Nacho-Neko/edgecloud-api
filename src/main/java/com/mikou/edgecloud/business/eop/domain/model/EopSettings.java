package com.mikou.edgecloud.business.eop.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * EOP 应用设置（监听器配置已迁移至 Edge 层 EdgeProtocol）
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EopSettings {

    /** EOP 允许使用的端口范围，例如 "1024:65535" */
    private String allowedPortRange;

    public EopSettings() {}

    public String getAllowedPortRange() { return allowedPortRange; }
    public EopSettings setAllowedPortRange(String allowedPortRange) {
        this.allowedPortRange = allowedPortRange;
        return this;
    }
}