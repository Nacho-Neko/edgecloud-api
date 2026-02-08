package com.mikou.edgecloud.edge.domain.model;

import com.mikou.edgecloud.common.enums.FeatureType;

import java.util.HashMap;
import java.util.Map;

/**
 * Edge 节点能力配置
 */
public class EdgeFeatures {

    private Map<String, FeatureConfig> features = new HashMap<>();

    public boolean isEnabled(FeatureType feature) {
        FeatureConfig config = features.get(feature.name());
        return config != null && config.isEnabled();
    }

    public static class FeatureConfig {
        private boolean enabled;
        private Map<String, Object> config;  // 每个能力的具体配置

        public FeatureConfig() {}

        public FeatureConfig(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
    }

    public Map<String, FeatureConfig> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, FeatureConfig> capabilities) {
        this.features = capabilities;
    }

    // 便捷方法
    public boolean hasFeature(String capabilityName) {
        FeatureConfig config = features.get(capabilityName);
        return config != null && config.isEnabled();
    }

    public void enableFeature(String capabilityName) {
        FeatureConfig config = features.get(capabilityName);
        if (config == null) {
            throw new IllegalStateException("Capability not created yet: " + capabilityName);
        }
        config.setEnabled(true);
    }

    public void disableFeature(String capabilityName) {
        FeatureConfig config = features.get(capabilityName);
        if (config != null) {
            config.setEnabled(false);
        }
    }

    public void createFeature(String capabilityName) {
        if (features.containsKey(capabilityName)) {
            throw new IllegalStateException("Capability already exists: " + capabilityName);
        }
        features.put(capabilityName, new FeatureConfig(true));
    }

    public void destroyFeature(String capabilityName) {
        FeatureConfig config = features.get(capabilityName);
        if (config == null) {
            throw new IllegalStateException("Capability does not exist: " + capabilityName);
        }
        if (config.isEnabled()) {
            throw new IllegalStateException("Cannot destroy enabled capability: " + capabilityName);
        }
        features.remove(capabilityName);
    }

    public boolean featureExists(String capabilityName) {
        return features.containsKey(capabilityName);
    }
}