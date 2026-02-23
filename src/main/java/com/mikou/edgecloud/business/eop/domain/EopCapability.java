package com.mikou.edgecloud.business.eop.domain;

import com.mikou.edgecloud.business.domain.BusinessCapability;
import org.springframework.stereotype.Component;

/**
 * EOP 业务能力
 * 自动注册到 BusinessCapabilityRegistry
 */
@Component
public class EopCapability implements BusinessCapability {

    @Override
    public String getCode() {
        return "EOP";
    }

    @Override
    public String getName() {
        return "边缘运营平台";
    }

    @Override
    public String getDescription() {
        return "Edge Operation Platform - 提供边缘节点资源的商业化运营服务";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}
