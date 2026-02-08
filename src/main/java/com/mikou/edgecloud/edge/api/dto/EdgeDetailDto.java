package com.mikou.edgecloud.edge.api.dto;

import com.mikou.edgecloud.edge.domain.enums.EdgeStatus;
import java.util.List;
import java.util.UUID;

public class EdgeDetailDto {
    private UUID edgeTag;
    private String name;
    private RegionTreeDto region;
    private EdgeStatus status;
    private boolean online;
    // 修改：返回能力详情列表（包含启用/禁用状态）
    private List<CapabilityDto> capabilities;

    // 修改：返回网卡列表（每个网卡包含其 IP 列表）
    private List<EdgeNicDto> nics;

    public UUID getEdgeTag() {
        return edgeTag;
    }

    public EdgeDetailDto setEdgeTag(UUID edgeTag) {
        this.edgeTag = edgeTag;
        return this;
    }

    public String getName() {
        return name;
    }

    public EdgeDetailDto setName(String name) {
        this.name = name;
        return this;
    }

    public RegionTreeDto getRegion() {
        return region;
    }

    public EdgeDetailDto setRegion(RegionTreeDto region) {
        this.region = region;
        return this;
    }

    public EdgeStatus getStatus() {
        return status;
    }

    public EdgeDetailDto setStatus(EdgeStatus status) {
        this.status = status;
        return this;
    }

    public boolean isOnline() {
        return online;
    }

    public EdgeDetailDto setOnline(boolean online) {
        this.online = online;
        return this;
    }

    public List<CapabilityDto> getCapabilities() {
        return capabilities;
    }

    public EdgeDetailDto setCapabilities(List<CapabilityDto> capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public List<EdgeNicDto> getNics() {
        return nics;
    }

    public EdgeDetailDto setNics(List<EdgeNicDto> nics) {
        this.nics = nics;
        return this;
    }
}