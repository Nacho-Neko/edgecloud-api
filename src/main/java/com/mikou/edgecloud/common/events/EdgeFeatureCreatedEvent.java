package com.mikou.edgecloud.common.events;

import com.mikou.edgecloud.common.enums.FeatureType;

/**
 * 领域事件：某个 Edge 创建了某个 Feature
 */
public record EdgeFeatureCreatedEvent(Integer edgeId, String edgeTag, FeatureType featureType) { }
