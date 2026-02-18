package com.mikou.edgecloud.platform.api.dto;

/**
 * 边缘节点统计
 */
public class EdgeStatisticsDto {
    private Long totalNodes;
    private Long onlineNodes;
    private Long offlineNodes;
    private Long totalRegions;
    
    public Long getTotalNodes() {
        return totalNodes;
    }
    
    public EdgeStatisticsDto setTotalNodes(Long totalNodes) {
        this.totalNodes = totalNodes;
        return this;
    }
    
    public Long getOnlineNodes() {
        return onlineNodes;
    }
    
    public EdgeStatisticsDto setOnlineNodes(Long onlineNodes) {
        this.onlineNodes = onlineNodes;
        return this;
    }
    
    public Long getOfflineNodes() {
        return offlineNodes;
    }
    
    public EdgeStatisticsDto setOfflineNodes(Long offlineNodes) {
        this.offlineNodes = offlineNodes;
        return this;
    }
    
    public Long getTotalRegions() {
        return totalRegions;
    }
    
    public EdgeStatisticsDto setTotalRegions(Long totalRegions) {
        this.totalRegions = totalRegions;
        return this;
    }
}
