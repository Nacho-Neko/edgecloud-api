package com.mikou.edgecloud.edge.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.Instant;
import java.util.Objects;

@TableName("edge_status")
public class EdgeStatusEntity {

    @TableId(type = IdType.INPUT)
    private Integer edgeId;

    @TableField("sample_time")
    private Instant sampleTime;

    @TableField("cpu_usage")
    private Double cpuUsage = 0.0;

    @TableField("used_memory")
    private Long usedMemory = 0L;

    @TableField("total_memory")
    private Long totalMemory = 0L;

    public Integer getEdgeId() {
        return edgeId;
    }

    public EdgeStatusEntity setEdgeId(Integer edgeId) {
        this.edgeId = edgeId;
        return this;
    }

    public Instant getSampleTime() {
        return sampleTime;
    }

    public EdgeStatusEntity setSampleTime(Instant sampleTime) {
        this.sampleTime = sampleTime;
        return this;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public EdgeStatusEntity setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
        return this;
    }

    public Long getUsedMemory() {
        return usedMemory;
    }

    public EdgeStatusEntity setUsedMemory(Long usedMemory) {
        this.usedMemory = usedMemory;
        return this;
    }

    public Long getTotalMemory() {
        return totalMemory;
    }

    public EdgeStatusEntity setTotalMemory(Long totalMemory) {
        this.totalMemory = totalMemory;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeStatusEntity that = (EdgeStatusEntity) o;
        return Objects.equals(edgeId, that.edgeId) &&
                Objects.equals(sampleTime, that.sampleTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(edgeId, sampleTime);
    }
}