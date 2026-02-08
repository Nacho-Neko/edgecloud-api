package com.mikou.edgecloud.edge.api.dto;

import com.mikou.edgecloud.edge.domain.enums.EdgeStatus;
import java.util.UUID;

public class EdgeItemDto {
    private Integer id;
    private UUID edgeTag;
    private String name;
    private Integer regionId;
    private EdgeStatus status;
    private boolean online;

    public Integer getId() { return id; }
    public EdgeItemDto setId(Integer id) { this.id = id; return this; }
    public UUID getEdgeTag() { return edgeTag; }
    public EdgeItemDto setEdgeTag(UUID edgeTag) { this.edgeTag = edgeTag; return this; }
    public String getName() { return name; }
    public EdgeItemDto setName(String name) { this.name = name; return this; }
    public Integer getRegionId() { return regionId; }
    public EdgeItemDto setRegionId(Integer regionId) { this.regionId = regionId; return this; }
    public EdgeStatus getStatus() { return status; }
    public EdgeItemDto setStatus(EdgeStatus status) { this.status = status; return this; }
    public boolean isOnline() { return online; }
    public EdgeItemDto setOnline(boolean online) { this.online = online; return this; }
}