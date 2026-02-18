package com.mikou.edgecloud.platform.api.dto;

/**
 * 管理员统计
 */
public class AdminStatisticsDto {
    private Long totalAdmins;
    private Long activeAdmins;
    private Long totalRoles;
    
    public Long getTotalAdmins() {
        return totalAdmins;
    }
    
    public AdminStatisticsDto setTotalAdmins(Long totalAdmins) {
        this.totalAdmins = totalAdmins;
        return this;
    }
    
    public Long getActiveAdmins() {
        return activeAdmins;
    }
    
    public AdminStatisticsDto setActiveAdmins(Long activeAdmins) {
        this.activeAdmins = activeAdmins;
        return this;
    }
    
    public Long getTotalRoles() {
        return totalRoles;
    }
    
    public AdminStatisticsDto setTotalRoles(Long totalRoles) {
        this.totalRoles = totalRoles;
        return this;
    }
}
