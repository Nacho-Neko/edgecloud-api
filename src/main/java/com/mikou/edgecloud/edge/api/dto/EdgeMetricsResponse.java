package com.mikou.edgecloud.edge.api.dto;

import java.util.List;

/**
 * Edge 监控指标响应 DTO
 * 包含时序数据和元信息
 */
public class EdgeMetricsResponse {
    private Integer edgeId;
    private String edgeName;
    private Long totalPoints;           // 总数据点数
    private List<EdgeMetricsPointDto> metrics;

    public Integer getEdgeId() {
        return edgeId;
    }

    public EdgeMetricsResponse setEdgeId(Integer edgeId) {
        this.edgeId = edgeId;
        return this;
    }

    public String getEdgeName() {
        return edgeName;
    }

    public EdgeMetricsResponse setEdgeName(String edgeName) {
        this.edgeName = edgeName;
        return this;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }

    public EdgeMetricsResponse setTotalPoints(Long totalPoints) {
        this.totalPoints = totalPoints;
        return this;
    }

    public List<EdgeMetricsPointDto> getMetrics() {
        return metrics;
    }

    public EdgeMetricsResponse setMetrics(List<EdgeMetricsPointDto> metrics) {
        this.metrics = metrics;
        this.totalPoints = metrics != null ? (long) metrics.size() : 0L;
        return this;
    }
}
