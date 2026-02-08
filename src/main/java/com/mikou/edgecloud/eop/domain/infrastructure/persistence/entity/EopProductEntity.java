package com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@TableName("eop_product")
public class EopProductEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("tag")
    private UUID tag;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("introduction")
    private String introduction;

    @TableField("monthly_price")
    private BigDecimal monthlyPrice;

    @TableField("max_outbound_count")
    private Integer maxOutboundCount;

    @TableField("max_inbound_count")
    private Integer maxInboundCount;

    @TableField("outbound_max_connections")
    private Integer outboundMaxConnections = 128;

    @TableField("inbound_max_connections")
    private Integer inboundMaxConnections = 128;

    @TableField("allowed_entry_level")
    private Integer allowedEntryLevel;

    @TableField("max_origin_targets")
    private Integer maxOriginTargets;

    @TableField("created_at")
    private Instant createdAt;

    @TableField("updated_at")
    private Instant updatedAt;

    @TableField("removed_at")
    private Instant removedAt;

    public Integer getId() { return id; }
    public EopProductEntity setId(Integer id) { this.id = id; return this; }

    public UUID getTag() { return tag; }
    public EopProductEntity setTag(UUID tag) { this.tag = tag; return this; }

    public String getName() { return name; }
    public EopProductEntity setName(String name) { this.name = name; return this; }

    public String getDescription() { return description; }
    public EopProductEntity setDescription(String description) { this.description = description; return this; }

    public String getIntroduction() { return introduction; }
    public EopProductEntity setIntroduction(String introduction) { this.introduction = introduction; return this; }

    public BigDecimal getMonthlyPrice() { return monthlyPrice; }
    public EopProductEntity setMonthlyPrice(BigDecimal monthlyPrice) { this.monthlyPrice = monthlyPrice; return this; }

    public Integer getMaxOutboundCount() { return maxOutboundCount; }
    public EopProductEntity setMaxOutboundCount(Integer maxOutboundCount) { this.maxOutboundCount = maxOutboundCount; return this; }

    public Integer getMaxInboundCount() { return maxInboundCount; }
    public EopProductEntity setMaxInboundCount(Integer maxInboundCount) { this.maxInboundCount = maxInboundCount; return this; }

    public Integer getOutboundMaxConnections() { return outboundMaxConnections; }
    public EopProductEntity setOutboundMaxConnections(Integer outboundMaxConnections) { this.outboundMaxConnections = outboundMaxConnections; return this; }

    public Integer getInboundMaxConnections() { return inboundMaxConnections; }
    public EopProductEntity setInboundMaxConnections(Integer inboundMaxConnections) { this.inboundMaxConnections = inboundMaxConnections; return this; }

    public Integer getAllowedEntryLevel() { return allowedEntryLevel; }
    public EopProductEntity setAllowedEntryLevel(Integer allowedEntryLevel) { this.allowedEntryLevel = allowedEntryLevel; return this; }

    public Integer getMaxOriginTargets() { return maxOriginTargets; }
    public EopProductEntity setMaxOriginTargets(Integer maxOriginTargets) { this.maxOriginTargets = maxOriginTargets; return this; }

    public Instant getCreatedAt() { return createdAt; }
    public EopProductEntity setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }

    public Instant getUpdatedAt() { return updatedAt; }
    public EopProductEntity setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

    public Instant getRemovedAt() { return removedAt; }
    public EopProductEntity setRemovedAt(Instant removedAt) { this.removedAt = removedAt; return this; }
}