package com.mikou.edgecloud.eop.api.dto;

import java.math.BigDecimal;

public class UpdateEopProductRequest {
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
    public UpdateEopProductRequest setName(String name) { this.name = name; return this; }

    public String getDescription() { return description; }
    public UpdateEopProductRequest setDescription(String description) { this.description = description; return this; }

    public String getIntroduction() { return introduction; }
    public UpdateEopProductRequest setIntroduction(String introduction) { this.introduction = introduction; return this; }

    public BigDecimal getMonthlyPrice() { return monthlyPrice; }
    public UpdateEopProductRequest setMonthlyPrice(BigDecimal monthlyPrice) { this.monthlyPrice = monthlyPrice; return this; }

    public Integer getMaxOutboundCount() { return maxOutboundCount; }
    public UpdateEopProductRequest setMaxOutboundCount(Integer maxOutboundCount) { this.maxOutboundCount = maxOutboundCount; return this; }

    public Integer getMaxInboundCount() { return maxInboundCount; }
    public UpdateEopProductRequest setMaxInboundCount(Integer maxInboundCount) { this.maxInboundCount = maxInboundCount; return this; }

    public Integer getOutboundMaxConnections() { return outboundMaxConnections; }
    public UpdateEopProductRequest setOutboundMaxConnections(Integer outboundMaxConnections) { this.outboundMaxConnections = outboundMaxConnections; return this; }

    public Integer getInboundMaxConnections() { return inboundMaxConnections; }
    public UpdateEopProductRequest setInboundMaxConnections(Integer inboundMaxConnections) { this.inboundMaxConnections = inboundMaxConnections; return this; }

    public Integer getAllowedEntryLevel() { return allowedEntryLevel; }
    public UpdateEopProductRequest setAllowedEntryLevel(Integer allowedEntryLevel) { this.allowedEntryLevel = allowedEntryLevel; return this; }

    public Integer getMaxOriginTargets() { return maxOriginTargets; }
    public UpdateEopProductRequest setMaxOriginTargets(Integer maxOriginTargets) { this.maxOriginTargets = maxOriginTargets; return this; }
}