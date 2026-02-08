package com.mikou.edgecloud.edge.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mikou.edgecloud.edge.domain.enums.AreaLevel;
import java.time.Instant;

@TableName("edge_area")
public class EdgeAreaEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("parent_id")
    private Integer parentId;

    @TableField("level")
    private AreaLevel level;

    @TableField("code")
    private String code;

    @TableField("name")
    private String name;

    @TableField("created_at")
    private Instant createdAt;

    @TableField("updated_at")
    private Instant updatedAt;

    public Integer getId() {
        return id;
    }

    public EdgeAreaEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getParentId() {
        return parentId;
    }

    public EdgeAreaEntity setParentId(Integer parentId) {
        this.parentId = parentId;
        return this;
    }

    public AreaLevel getLevel() {
        return level;
    }

    public EdgeAreaEntity setLevel(AreaLevel level) {
        this.level = level;
        return this;
    }

    public String getCode() {
        return code;
    }

    public EdgeAreaEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public EdgeAreaEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public EdgeAreaEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public EdgeAreaEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}