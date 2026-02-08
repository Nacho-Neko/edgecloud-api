package com.mikou.edgecloud.edge.domain.service;

import com.mikou.edgecloud.common.enums.FeatureType;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Edge 能力管理领域服务
 * 注意：这是接口，不要加 @Service 注解
 */
@Service
@Primary
public interface EdgeFeatureService {

    void createFeature(String edgeTag, FeatureType capability);

    void enableFeature(String edgeTag, FeatureType capability);

    void disableFeature(String edgeTag, FeatureType capability);

    void destroyFeature(String edgeTag, FeatureType capability);

    boolean hasFeature(String edgeTag, FeatureType capability);

    Set<String> getEnabledFeatures(String edgeTag);

    void setFeatures(String edgeTag, Set<FeatureType> capabilities);
}