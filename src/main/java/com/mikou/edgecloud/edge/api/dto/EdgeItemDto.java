package com.mikou.edgecloud.edge.api.dto;

import com.mikou.edgecloud.edge.domain.enums.EdgeStatus;
import com.mikou.edgecloud.edge.domain.model.EdgeFeatures;
import java.time.Instant;
import java.util.UUID;

public class EdgeItemDto {
    private Integer id;
    private UUID edgeTag;
    private String name;
    private RegionTreeDto region;
    private EdgeStatus status;
    private boolean online;
    private EdgeStatusDto latestStatus;
    private EdgeFeatures features;
    private Instant createdAt;
    private Instant updatedAt;
    
    // 硬件信息
    private Integer cpuCores;
    private String cpuModel;
    private Long totalMemory;
    
    // 系统信息
    private String osType;
    private String osVersion;
    private String osArch;
    
    // Edge 版本
    private String edgeVersion;

    public Integer getId() { return id; }
    public EdgeItemDto setId(Integer id) { this.id = id; return this; }
    public UUID getEdgeTag() { return edgeTag; }
    public EdgeItemDto setEdgeTag(UUID edgeTag) { this.edgeTag = edgeTag; return this; }
    public String getName() { return name; }
    public EdgeItemDto setName(String name) { this.name = name; return this; }
    public RegionTreeDto getRegion() { return region; }
    public EdgeItemDto setRegion(RegionTreeDto region) { this.region = region; return this; }
    public EdgeStatus getStatus() { return status; }
    public EdgeItemDto setStatus(EdgeStatus status) { this.status = status; return this; }
    public boolean isOnline() { return online; }
    public EdgeItemDto setOnline(boolean online) { this.online = online; return this; }
    public EdgeStatusDto getLatestStatus() { return latestStatus; }
    public EdgeItemDto setLatestStatus(EdgeStatusDto latestStatus) { this.latestStatus = latestStatus; return this; }
    public EdgeFeatures getFeatures() { return features; }
    public EdgeItemDto setFeatures(EdgeFeatures features) { this.features = features; return this; }
    public Instant getCreatedAt() { return createdAt; }
    public EdgeItemDto setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }
    public Instant getUpdatedAt() { return updatedAt; }
    public EdgeItemDto setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }
    
    public Integer getCpuCores() { return cpuCores; }
    public EdgeItemDto setCpuCores(Integer cpuCores) { this.cpuCores = cpuCores; return this; }
    public String getCpuModel() { return cpuModel; }
    public EdgeItemDto setCpuModel(String cpuModel) { this.cpuModel = cpuModel; return this; }
    public Long getTotalMemory() { return totalMemory; }
    public EdgeItemDto setTotalMemory(Long totalMemory) { this.totalMemory = totalMemory; return this; }
    
    public String getOsType() { return osType; }
    public EdgeItemDto setOsType(String osType) { this.osType = osType; return this; }
    public String getOsVersion() { return osVersion; }
    public EdgeItemDto setOsVersion(String osVersion) { this.osVersion = osVersion; return this; }
    public String getOsArch() { return osArch; }
    public EdgeItemDto setOsArch(String osArch) { this.osArch = osArch; return this; }
    
    public String getEdgeVersion() { return edgeVersion; }
    public EdgeItemDto setEdgeVersion(String edgeVersion) { this.edgeVersion = edgeVersion; return this; }
}