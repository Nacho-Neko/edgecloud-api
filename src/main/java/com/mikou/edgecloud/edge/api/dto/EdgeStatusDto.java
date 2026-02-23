package com.mikou.edgecloud.edge.api.dto;

import java.time.Instant;

/**
 * Edge 状态统计信息 DTO
 */
public class EdgeStatusDto {
    private Instant sampleTime;
    private Double cpuUsage;
    private Long usedMemory;
    private Long totalMemory;

    public Instant getSampleTime() {
        return sampleTime;
    }

    public EdgeStatusDto setSampleTime(Instant sampleTime) {
        this.sampleTime = sampleTime;
        return this;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public EdgeStatusDto setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
        return this;
    }

    public Long getUsedMemory() {
        return usedMemory;
    }

    public EdgeStatusDto setUsedMemory(Long usedMemory) {
        this.usedMemory = usedMemory;
        return this;
    }

    public Long getTotalMemory() {
        return totalMemory;
    }

    public EdgeStatusDto setTotalMemory(Long totalMemory) {
        this.totalMemory = totalMemory;
        return this;
    }
}
