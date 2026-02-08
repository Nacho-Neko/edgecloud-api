package com.mikou.edgecloud.eop.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EopServiceEntitlements {
    
    @JsonProperty("MaxOutboundCount")
    private Integer maxOutboundCount;
    
    @JsonProperty("MaxInboundCount")
    private Integer maxInboundCount;
    
    @JsonProperty("OutboundMaxConnections")
    private Integer outboundMaxConnections;
    
    @JsonProperty("InboundMaxConnections")
    private Integer inboundMaxConnections;
    
    @JsonProperty("MaxOriginTargets")
    private Integer maxOriginTargets;

    public Integer getMaxOutboundCount() {
        return maxOutboundCount;
    }

    public void setMaxOutboundCount(Integer maxOutboundCount) {
        this.maxOutboundCount = maxOutboundCount;
    }

    public Integer getMaxInboundCount() {
        return maxInboundCount;
    }

    public void setMaxInboundCount(Integer maxInboundCount) {
        this.maxInboundCount = maxInboundCount;
    }

    public Integer getOutboundMaxConnections() {
        return outboundMaxConnections;
    }

    public void setOutboundMaxConnections(Integer outboundMaxConnections) {
        this.outboundMaxConnections = outboundMaxConnections;
    }

    public Integer getInboundMaxConnections() {
        return inboundMaxConnections;
    }

    public void setInboundMaxConnections(Integer inboundMaxConnections) {
        this.inboundMaxConnections = inboundMaxConnections;
    }

    public Integer getMaxOriginTargets() {
        return maxOriginTargets;
    }

    public void setMaxOriginTargets(Integer maxOriginTargets) {
        this.maxOriginTargets = maxOriginTargets;
    }
}
