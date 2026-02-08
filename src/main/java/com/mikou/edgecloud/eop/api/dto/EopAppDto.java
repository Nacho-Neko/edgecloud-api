package com.mikou.edgecloud.eop.api.dto;

import com.mikou.edgecloud.edge.api.dto.EdgeItemDto;
import com.mikou.edgecloud.eop.domain.model.EopSettings;
import java.time.Instant;
import java.util.UUID;

public class EopAppDto {
    private Integer id;
    private UUID eopTag;
    private UUID edgeTag;
    private EdgeItemDto edge;
    private EopSettings settings;
    private Instant createdAt;
    private Instant updatedAt;

    public Integer getId() { return id; }
    public EopAppDto setId(Integer id) { this.id = id; return this; }

    public UUID getEopTag() { return eopTag; }
    public EopAppDto setEopTag(UUID eopTag) { this.eopTag = eopTag; return this; }

    public UUID getEdgeTag() { return edgeTag; }
    public EopAppDto setEdgeTag(UUID edgeTag) { this.edgeTag = edgeTag; return this; }

    public EdgeItemDto getEdge() { return edge; }
    public EopAppDto setEdge(EdgeItemDto edge) { this.edge = edge; return this; }

    public EopSettings getSettings() { return settings; }
    public EopAppDto setSettings(EopSettings settings) { this.settings = settings; return this; }

    public Instant getCreatedAt() { return createdAt; }
    public EopAppDto setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }

    public Instant getUpdatedAt() { return updatedAt; }
    public EopAppDto setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }
}
