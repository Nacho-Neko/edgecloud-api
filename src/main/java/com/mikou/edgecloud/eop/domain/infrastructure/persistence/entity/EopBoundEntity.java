package com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.mikou.edgecloud.eop.domain.enums.EopDirection;
import com.mikou.edgecloud.eop.domain.model.EopBoundParams;

import java.time.Instant;
import java.util.UUID;

@TableName(value = "eop_bound", autoResultMap = true)
public class EopBoundEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("tag")
    private UUID tag;

    @TableField("service_id")
    private Integer serviceId;

    @TableField("eop_id")
    private Integer eopId;

    @TableField("owner_id")
    private UUID ownerId;

    @TableField("product_id")
    private Integer productId;

    @TableField("direction")
    private EopDirection direction;

    @TableField("addr_id")
    private Integer addrId;

    @TableField("max_connections")
    private Integer maxConnections = 128;

    @TableField(value = "extra_params_json", typeHandler = JacksonTypeHandler.class)
    private EopBoundParams extraParams;

    @TableField("created_at")
    private Instant createdAt;

    @TableField("updated_at")
    private Instant updatedAt;

    @TableField("removed_at")
    private Instant removedAt;

    public Integer getId() { return id; }
    public EopBoundEntity setId(Integer id) { this.id = id; return this; }

    public UUID getTag() { return tag; }
    public EopBoundEntity setTag(UUID tag) { this.tag = tag; return this; }

    public Integer getServiceId() { return serviceId; }
    public EopBoundEntity setServiceId(Integer serviceId) { this.serviceId = serviceId; return this; }

    public Integer getEopId() { return eopId; }
    public EopBoundEntity setEopId(Integer eopId) { this.eopId = eopId; return this; }

    public UUID getOwnerId() { return ownerId; }
    public EopBoundEntity setOwnerId(UUID ownerId) { this.ownerId = ownerId; return this; }

    public Integer getProductId() { return productId; }
    public EopBoundEntity setProductId(Integer productId) { this.productId = productId; return this; }

    public EopDirection getDirection() { return direction; }
    public EopBoundEntity setDirection(EopDirection direction) { this.direction = direction; return this; }

    public Integer getAddrId() { return addrId; }
    public EopBoundEntity setAddrId(Integer addrId) { this.addrId = addrId; return this; }

    public Integer getMaxConnections() { return maxConnections; }
    public EopBoundEntity setMaxConnections(Integer maxConnections) { this.maxConnections = maxConnections; return this; }

    public EopBoundParams getExtraParams() { return extraParams; }
    public EopBoundEntity setExtraParams(EopBoundParams extraParams) { this.extraParams = extraParams; return this; }

    public Instant getCreatedAt() { return createdAt; }
    public EopBoundEntity setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }

    public Instant getUpdatedAt() { return updatedAt; }
    public EopBoundEntity setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

    public Instant getRemovedAt() { return removedAt; }
    public EopBoundEntity setRemovedAt(Instant removedAt) { this.removedAt = removedAt; return this; }
}