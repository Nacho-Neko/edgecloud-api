package com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.mikou.edgecloud.eop.domain.model.EopSettings;

import java.time.Instant;
import java.util.UUID;

@TableName(value = "eop_app", autoResultMap = true)
public class EopAppEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private UUID eopTag;

    private Integer edgeId;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private EopSettings settings;

    private Instant createdAt;

    private Instant updatedAt;

    public EopAppEntity() {
    }

    public Integer getId() {
        return id;
    }

    public EopAppEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public UUID getEopTag() {
        return eopTag;
    }

    public EopAppEntity setEopTag(UUID eopTag) {
        this.eopTag = eopTag;
        return this;
    }

    public Integer getEdgeId() {
        return edgeId;
    }

    public EopAppEntity setEdgeId(Integer edgeId) {
        this.edgeId = edgeId;
        return this;
    }

    public EopSettings getSettings() {
        return settings;
    }

    public EopAppEntity setSettings(EopSettings settings) {
        this.settings = settings;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public EopAppEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public EopAppEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}