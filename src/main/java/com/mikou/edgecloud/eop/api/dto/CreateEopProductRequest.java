package com.mikou.edgecloud.eop.api.dto;

import java.math.BigDecimal;

public class CreateEopProductRequest {
    private String name;
    private String description;
    private String introduction;
    private BigDecimal monthlyPrice;

    private Integer maxOutboundCount;
    private Integer maxInboundCount;
    private Integer outboundMaxConnections;
    private Integer inboundMaxConnections;
    private Integer allowedEntryLevel;
    private Integer maxOriginTargets;

    public String getName() { return name; }
    public CreateEopProductRequest setName(String name) { this.name = name; return this; }

    public String getDescription() { return description; }
    public CreateEopProductRequest setDescription(String description) { this.description = description; return this; }

    public String getIntroduction() { return introduction; }
    public CreateEopProductRequest setIntroduction(String introduction) { this.introduction = introduction; return this; }

    public BigDecimal getMonthlyPrice() { return monthlyPrice; }
    public CreateEopProductRequest setMonthlyPrice(BigDecimal monthlyPrice) { this.monthlyPrice = monthlyPrice; return this; }

    public Integer getMaxOutboundCount() { return maxOutboundCount; }
    public CreateEopProductRequest setMaxOutboundCount(Integer maxOutboundCount) { this.maxOutboundCount = maxOutboundCount; return this; }

    public Integer getMaxInboundCount() { return maxInboundCount; }
    public CreateEopProductRequest setMaxInboundCount(Integer maxInboundCount) { this.maxInboundCount = maxInboundCount; return this; }

    public Integer getOutboundMaxConnections() { return outboundMaxConnections; }
    public CreateEopProductRequest setOutboundMaxConnections(Integer outboundMaxConnections) { this.outboundMaxConnections = outboundMaxConnections; return this; }

    public Integer getInboundMaxConnections() { return inboundMaxConnections; }
    public CreateEopProductRequest setInboundMaxConnections(Integer inboundMaxConnections) { this.inboundMaxConnections = inboundMaxConnections; return this; }

    public Integer getAllowedEntryLevel() { return allowedEntryLevel; }
    public CreateEopProductRequest setAllowedEntryLevel(Integer allowedEntryLevel) { this.allowedEntryLevel = allowedEntryLevel; return this; }

    public Integer getMaxOriginTargets() { return maxOriginTargets; }
    public CreateEopProductRequest setMaxOriginTargets(Integer maxOriginTargets) { this.maxOriginTargets = maxOriginTargets; return this; }
}