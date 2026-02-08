package com.mikou.edgecloud.edge.api.dto;

import java.time.Instant;
import java.util.List;

public class EdgeNicDto {
    private String macAddress;
    private String nicName;
    private Instant createdAt;
    private List<NicIpDto> ips;

    public String getMacAddress() {
        return macAddress;
    }

    public EdgeNicDto setMacAddress(String macAddress) {
        this.macAddress = macAddress;
        return this;
    }

    public String getNicName() {
        return nicName;
    }

    public EdgeNicDto setNicName(String nicName) {
        this.nicName = nicName;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public EdgeNicDto setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public List<NicIpDto> getIps() {
        return ips;
    }

    public EdgeNicDto setIps(List<NicIpDto> ips) {
        this.ips = ips;
        return this;
    }
}