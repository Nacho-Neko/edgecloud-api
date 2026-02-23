package com.mikou.edgecloud.edge.api.dto;

import java.time.Instant;

/**
 * Edge 系统信息 DTO
 * 包含硬件配置、操作系统和 Edge 程序版本信息
 */
public class EdgeSystemInfoDto {
    private Integer edgeId;
    
    // 硬件信息
    private Integer cpuCores;
    private String cpuModel;
    private Long totalMemory;  // 字节
    
    // 操作系统信息
    private String osType;
    private String osVersion;
    private String osArch;
    
    // Edge 程序版本
    private String edgeVersion;
    
    // 信息更新时间
    private Instant updatedAt;

    public Integer getEdgeId() {
        return edgeId;
    }

    public EdgeSystemInfoDto setEdgeId(Integer edgeId) {
        this.edgeId = edgeId;
        return this;
    }

    public Integer getCpuCores() {
        return cpuCores;
    }

    public EdgeSystemInfoDto setCpuCores(Integer cpuCores) {
        this.cpuCores = cpuCores;
        return this;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public EdgeSystemInfoDto setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
        return this;
    }

    public Long getTotalMemory() {
        return totalMemory;
    }

    public EdgeSystemInfoDto setTotalMemory(Long totalMemory) {
        this.totalMemory = totalMemory;
        return this;
    }

    public String getOsType() {
        return osType;
    }

    public EdgeSystemInfoDto setOsType(String osType) {
        this.osType = osType;
        return this;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public EdgeSystemInfoDto setOsVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }

    public String getOsArch() {
        return osArch;
    }

    public EdgeSystemInfoDto setOsArch(String osArch) {
        this.osArch = osArch;
        return this;
    }

    public String getEdgeVersion() {
        return edgeVersion;
    }

    public EdgeSystemInfoDto setEdgeVersion(String edgeVersion) {
        this.edgeVersion = edgeVersion;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public EdgeSystemInfoDto setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * 获取总内存（GB）
     */
    public Double getTotalMemoryGB() {
        return totalMemory != null ? totalMemory / (1024.0 * 1024.0 * 1024.0) : null;
    }

    /**
     * 获取完整的操作系统描述
     */
    public String getOsFullDescription() {
        StringBuilder sb = new StringBuilder();
        if (osType != null) sb.append(osType);
        if (osVersion != null) sb.append(" ").append(osVersion);
        if (osArch != null) sb.append(" (").append(osArch).append(")");
        return sb.toString().trim();
    }
}
