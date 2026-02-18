package com.mikou.edgecloud.platform.api.dto;

/**
 * 业务总览统计
 */
public class BusinessOverviewDto {
    private Long totalBusinesses;
    private Long runningBusinesses;
    private Long totalRevenue;
    private Long totalUsers;
    
    public Long getTotalBusinesses() {
        return totalBusinesses;
    }
    
    public BusinessOverviewDto setTotalBusinesses(Long totalBusinesses) {
        this.totalBusinesses = totalBusinesses;
        return this;
    }
    
    public Long getRunningBusinesses() {
        return runningBusinesses;
    }
    
    public BusinessOverviewDto setRunningBusinesses(Long runningBusinesses) {
        this.runningBusinesses = runningBusinesses;
        return this;
    }
    
    public Long getTotalRevenue() {
        return totalRevenue;
    }
    
    public BusinessOverviewDto setTotalRevenue(Long totalRevenue) {
        this.totalRevenue = totalRevenue;
        return this;
    }
    
    public Long getTotalUsers() {
        return totalUsers;
    }
    
    public BusinessOverviewDto setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
        return this;
    }
}
