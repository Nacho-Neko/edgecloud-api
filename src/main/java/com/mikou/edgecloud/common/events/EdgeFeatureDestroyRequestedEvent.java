package com.mikou.edgecloud.common.events;

import com.mikou.edgecloud.common.enums.FeatureType;

/**
 * 领域事件：请求销毁某个 Edge 的某个 Feature
 * 由功能领域（如 EOP）发起，Edge 领域监听并执行销毁
 */
public record EdgeFeatureDestroyRequestedEvent(String edgeTag, FeatureType featureType) { }
