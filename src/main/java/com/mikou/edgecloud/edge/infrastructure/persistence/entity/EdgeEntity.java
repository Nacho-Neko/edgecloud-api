package com.mikou.edgecloud.edge.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.mikou.edgecloud.edge.domain.model.EdgeFeatures;
import com.mikou.edgecloud.edge.domain.enums.EdgeStatus;
import java.time.Instant;
import java.util.UUID;

@TableName(value = "edge", autoResultMap = true)
public class EdgeEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "edge_tag")
    private UUID edgeTag;

    @TableField(value = "name")
    private String name;

    @TableField(value = "region_id")
    private Integer regionId;

    @TableField(value = "status")
    private EdgeStatus status;

    // 新增：能力配置（JSON 存储）
    @TableField(value = "features", typeHandler = JacksonTypeHandler.class)
    private EdgeFeatures features;

    @TableField(value = "created_at")
    private Instant createdAt;

    @TableField(value = "updated_at")
    private Instant updatedAt;

    public EdgeEntity() {
    }

    // ... existing getters/setters ...

    public EdgeFeatures getFeatures() {
        return features;
    }

    public EdgeEntity setFeatures(EdgeFeatures capabilities) {
        this.features = capabilities;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public EdgeEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public UUID getEdgeTag() {
        return edgeTag;
    }

    public EdgeEntity setEdgeTag(UUID edgeTag) {
        this.edgeTag = edgeTag;
        return this;
    }

    public String getName() {
        return name;
    }

    public EdgeEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public EdgeEntity setRegionId(Integer regionId) {
        this.regionId = regionId;
        return this;
    }

    public EdgeStatus getStatus() {
        return status;
    }

    public EdgeEntity setStatus(EdgeStatus status) {
        this.status = status;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public EdgeEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public EdgeEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}