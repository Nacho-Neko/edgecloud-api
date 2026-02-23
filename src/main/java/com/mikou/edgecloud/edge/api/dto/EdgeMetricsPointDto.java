package com.mikou.edgecloud.edge.api.dto;

import java.time.Instant;

/**
 * Edge 监控数据点 DTO
 * 用于时序图表展示
 */
public class EdgeMetricsPointDto {
    private Instant timestamp;
    private Double cpuUsage;      // CPU 占用率百分比 (0-100)
    private Double memoryUsage;   // 内存占用率百分比 (0-100)

    public Instant getTimestamp() {
        return timestamp;
    }

    public EdgeMetricsPointDto setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public EdgeMetricsPointDto setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
        return this;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public EdgeMetricsPointDto setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
        return this;
    }
}
