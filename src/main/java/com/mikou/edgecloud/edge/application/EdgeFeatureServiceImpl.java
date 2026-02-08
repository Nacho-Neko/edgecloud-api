package com.mikou.edgecloud.edge.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.common.enums.FeatureType;
import com.mikou.edgecloud.common.events.EdgeFeatureCreatedEvent;
import com.mikou.edgecloud.common.events.EdgeFeatureDisabledEvent;
import com.mikou.edgecloud.common.events.EdgeFeatureEnabledEvent;
import com.mikou.edgecloud.edge.domain.model.EdgeFeatures;
import com.mikou.edgecloud.edge.domain.service.EdgeFeatureService;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeMapper;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EdgeFeatureServiceImpl implements EdgeFeatureService {

    private final EdgeMapper edgeMapper;
    private final ApplicationEventPublisher eventPublisher;

    public EdgeFeatureServiceImpl(EdgeMapper edgeMapper, ApplicationEventPublisher eventPublisher) {
        this.edgeMapper = edgeMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void createFeature(String edgeTag, FeatureType feature) {
        EdgeEntity edge = getEdgeByTag(edgeTag);

        EdgeFeatures caps = edge.getFeatures();
        if (caps == null) {
            caps = new EdgeFeatures();
        }

        if (!caps.featureExists(feature.name())) {
            caps.createFeature(feature.name());
        }
        
        edge.setFeatures(caps);
        edge.setUpdatedAt(Instant.now());

        edgeMapper.updateById(edge);

        // 发布 Feature 创建事件
        eventPublisher.publishEvent(new EdgeFeatureCreatedEvent(edge.getId(), edgeTag, feature));
    }

    @Override
    @Transactional
    public void enableFeature(String edgeTag, FeatureType feature) {
        EdgeEntity edge = getEdgeByTag(edgeTag);

        EdgeFeatures caps = edge.getFeatures();
        if (caps == null) {
            throw new IllegalStateException("No capabilities found for edge: " + edgeTag);
        }

        if (!caps.featureExists(feature.name())) {
            throw new IllegalStateException("Feature not created yet: " + feature.name());
        }

        caps.enableFeature(feature.name());
        edge.setFeatures(caps);
        edge.setUpdatedAt(Instant.now());
        edgeMapper.updateById(edge);

        eventPublisher.publishEvent(new EdgeFeatureEnabledEvent(edge.getId(), edgeTag, feature));
    }

    @Override
    @Transactional
    public void disableFeature(String edgeTag, FeatureType feature) {
        EdgeEntity edge = getEdgeByTag(edgeTag);

        EdgeFeatures caps = edge.getFeatures();
        if (caps != null) {
            caps.disableFeature(feature.name());
            edge.setFeatures(caps);
            edge.setUpdatedAt(Instant.now());
            edgeMapper.updateById(edge);

            eventPublisher.publishEvent(new EdgeFeatureDisabledEvent(edge.getId(), edgeTag, feature));
        }
    }

    @Override
    @Transactional
    public void destroyFeature(String edgeTag, FeatureType feature) {
        EdgeEntity edge = getEdgeByTag(edgeTag);

        EdgeFeatures caps = edge.getFeatures();
        if (caps == null || !caps.featureExists(feature.name())) {
            // 如果不存在，则认为是已经销毁或无需处理，保持幂等
            return;
        }

        // 如果是启用状态，先禁用再销毁（强制回收）
        if (caps.isEnabled(feature)) {
            caps.disableFeature(feature.name());
        }

        caps.destroyFeature(feature.name());
        edge.setFeatures(caps);
        edge.setUpdatedAt(Instant.now());

        edgeMapper.updateById(edge);
    }

    @Override
    public boolean hasFeature(String edgeTag, FeatureType feature) {
        EdgeEntity edge = getEdgeByTag(edgeTag);
        EdgeFeatures caps = edge.getFeatures();
        return caps != null && caps.hasFeature(feature.name());
    }

    @Override
    public Set<String> getEnabledFeatures(String edgeTag) {
        EdgeEntity edge = getEdgeByTag(edgeTag);
        EdgeFeatures caps = edge.getFeatures();
        if (caps == null || caps.getFeatures() == null) {
            return Set.of();
        }

        return caps.getFeatures().entrySet().stream()
                .filter(entry -> entry.getValue().isEnabled())
                .map(entry -> entry.getKey())
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void setFeatures(String edgeTag, Set<FeatureType> features) {
        EdgeEntity edge = getEdgeByTag(edgeTag);

        EdgeFeatures caps = new EdgeFeatures();
        for (FeatureType feature : features) {
            caps.enableFeature(feature.name());
        }

        edge.setFeatures(caps);
        edge.setUpdatedAt(Instant.now());
        edgeMapper.updateById(edge);
    }

    private EdgeEntity getEdgeByTag(String edgeTag) {
        EdgeEntity edge = edgeMapper.selectOne(
                new LambdaQueryWrapper<EdgeEntity>()
                        .eq(EdgeEntity::getEdgeTag, edgeTag)
        );

        if (edge == null) {
            throw new IllegalArgumentException("Edge not found: " + edgeTag);
        }

        return edge;
    }
}