package com.mikou.edgecloud.edge.api.dto;

/**
 * 查询监控指标请求 DTO
 */
public class EdgeMetricsRequest {
    private Long startTime;  // 开始时间戳（毫秒），可选，不传则默认1小时前
    private Long endTime;    // 结束时间戳（毫秒），可选，不传则默认当前时间
    private Integer limit;   // 最大返回数据点数，可选

    public Long getStartTime() {
        return startTime;
    }

    public EdgeMetricsRequest setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public Long getEndTime() {
        return endTime;
    }

    public EdgeMetricsRequest setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public EdgeMetricsRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }
}
