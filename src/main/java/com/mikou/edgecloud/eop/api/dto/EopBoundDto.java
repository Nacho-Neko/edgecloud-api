package com.mikou.edgecloud.eop.api.dto;

import com.mikou.edgecloud.eop.domain.enums.EopDirection;
import com.mikou.edgecloud.eop.domain.model.EopBoundParams;
import java.time.Instant;
import java.util.UUID;

public class EopBoundDto {
    private UUID tag;
    private EopDirection direction;
    private Integer maxConnections;
    private EopBoundParams extraParams;
    private Instant createdAt;

    public UUID getTag() { return tag; }
    public EopBoundDto setTag(UUID tag) { this.tag = tag; return this; }

    public EopDirection getDirection() { return direction; }
    public EopBoundDto setDirection(EopDirection direction) { this.direction = direction; return this; }

    public Integer getMaxConnections() { return maxConnections; }
    public EopBoundDto setMaxConnections(Integer maxConnections) { this.maxConnections = maxConnections; return this; }

    public EopBoundParams getExtraParams() { return extraParams; }
    public EopBoundDto setExtraParams(EopBoundParams extraParams) { this.extraParams = extraParams; return this; }

    public Instant getCreatedAt() { return createdAt; }
    public EopBoundDto setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }
}
