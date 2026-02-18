package com.mikou.edgecloud.business.domain;

import java.util.Map;

/**
 * 业务统计数据
 */
public class BusinessStatistics {
    
    /**
     * 总用户数
     */
    private Long totalUsers;
    
    /**
     * 活跃用户数
     */
    private Long activeUsers;
    
    /**
     * 总订单数
     */
    private Long totalOrders;
    
    /**
     * 总收入（分）
     */
    private Long totalRevenue;
    
    /**
     * 自定义统计数据（业务特有指标）
     */
    private Map<String, Object> customMetrics;
    
    public Long getTotalUsers() {
        return totalUsers;
    }
    
    public BusinessStatistics setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
        return this;
    }
    
    public Long getActiveUsers() {
        return activeUsers;
    }
    
    public BusinessStatistics setActiveUsers(Long activeUsers) {
        this.activeUsers = activeUsers;
        return this;
    }
    
    public Long getTotalOrders() {
        return totalOrders;
    }
    
    public BusinessStatistics setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
        return this;
    }
    
    public Long getTotalRevenue() {
        return totalRevenue;
    }
    
    public BusinessStatistics setTotalRevenue(Long totalRevenue) {
        this.totalRevenue = totalRevenue;
        return this;
    }
    
    public Map<String, Object> getCustomMetrics() {
        return customMetrics;
    }
    
    public BusinessStatistics setCustomMetrics(Map<String, Object> customMetrics) {
        this.customMetrics = customMetrics;
        return this;
    }
}
