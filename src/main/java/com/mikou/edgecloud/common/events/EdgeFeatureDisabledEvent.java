package com.mikou.edgecloud.common.events;

import com.mikou.edgecloud.common.enums.FeatureType;

/**
 * 领域事件：某个 Edge 禁用了某个 Feature
 */
public record EdgeFeatureDisabledEvent(Integer edgeId, String edgeTag, FeatureType featureType) { }
