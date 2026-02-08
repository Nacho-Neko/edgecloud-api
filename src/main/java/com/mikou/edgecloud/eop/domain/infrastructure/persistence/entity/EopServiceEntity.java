package com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.mikou.edgecloud.eop.domain.enums.EopServiceStatus;
import com.mikou.edgecloud.eop.domain.model.EopServiceEntitlements;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@TableName(value = "eop_service", autoResultMap = true)
public class EopServiceEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("tag")
    private UUID tag;

    @TableField("owner_id")
    private UUID ownerId;

    @TableField("product_id")
    private Integer productId;

    @TableField("monthly_price")
    private BigDecimal monthlyPrice;

    @TableField(value = "entitlements_json", typeHandler = JacksonTypeHandler.class)
    private EopServiceEntitlements entitlements;

    @TableField("status")
    private EopServiceStatus status; // INACTIVE / OUTBOUND_READY / ACTIVE / SUSPENDED

    @TableField("expired_at")
    private Instant expiredAt;

    @TableField("created_at")
    private Instant createdAt;

    @TableField("updated_at")
    private Instant updatedAt;

    @TableField("removed_at")
    private Instant removedAt;

    public Integer getId() { return id; }
    public EopServiceEntity setId(Integer id) { this.id = id; return this; }

    public UUID getTag() { return tag; }
    public EopServiceEntity setTag(UUID tag) { this.tag = tag; return this; }

    public UUID getOwnerId() { return ownerId; }
    public EopServiceEntity setOwnerId(UUID ownerId) { this.ownerId = ownerId; return this; }

    public Integer getProductId() { return productId; }
    public EopServiceEntity setProductId(Integer productId) { this.productId = productId; return this; }

    public BigDecimal getMonthlyPrice() { return monthlyPrice; }
    public EopServiceEntity setMonthlyPrice(BigDecimal monthlyPrice) { this.monthlyPrice = monthlyPrice; return this; }

    public EopServiceEntitlements getEntitlements() { return entitlements; }
    public EopServiceEntity setEntitlements(EopServiceEntitlements entitlements) { this.entitlements = entitlements; return this; }

    public EopServiceStatus getStatus() { return status; }
    public EopServiceEntity setStatus(EopServiceStatus status) { this.status = status; return this; }

    public Instant getExpiredAt() { return expiredAt; }
    public EopServiceEntity setExpiredAt(Instant expiredAt) { this.expiredAt = expiredAt; return this; }

    public Instant getCreatedAt() { return createdAt; }
    public EopServiceEntity setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }

    public Instant getUpdatedAt() { return updatedAt; }
    public EopServiceEntity setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

    public Instant getRemovedAt() { return removedAt; }
    public EopServiceEntity setRemovedAt(Instant removedAt) { this.removedAt = removedAt; return this; }
}