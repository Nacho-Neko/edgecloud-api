package com.mikou.edgecloud.platform.api.dto;

import java.util.Map;

/**
 * 业务详细信息
 */
public class BusinessDetailDto {
    private String code;
    private String name;
    private String status;
    private Long totalUsers;
    private Long activeUsers;
    private Long totalOrders;
    private Long totalRevenue;
    private Map<String, Object> customMetrics;
    
    public String getCode() {
        return code;
    }
    
    public BusinessDetailDto setCode(String code) {
        this.code = code;
        return this;
    }
    
    public String getName() {
        return name;
    }
    
    public BusinessDetailDto setName(String name) {
        this.name = name;
        return this;
    }
    
    public String getStatus() {
        return status;
    }
    
    public BusinessDetailDto setStatus(String status) {
        this.status = status;
        return this;
    }
    
    public Long getTotalUsers() {
        return totalUsers;
    }
    
    public BusinessDetailDto setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
        return this;
    }
    
    public Long getActiveUsers() {
        return activeUsers;
    }
    
    public BusinessDetailDto setActiveUsers(Long activeUsers) {
        this.activeUsers = activeUsers;
        return this;
    }
    
    public Long getTotalOrders() {
        return totalOrders;
    }
    
    public BusinessDetailDto setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
        return this;
    }
    
    public Long getTotalRevenue() {
        return totalRevenue;
    }
    
    public BusinessDetailDto setTotalRevenue(Long totalRevenue) {
        this.totalRevenue = totalRevenue;
        return this;
    }
    
    public Map<String, Object> getCustomMetrics() {
        return customMetrics;
    }
    
    public BusinessDetailDto setCustomMetrics(Map<String, Object> customMetrics) {
        this.customMetrics = customMetrics;
        return this;
    }
}
