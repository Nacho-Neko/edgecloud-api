package com.mikou.edgecloud.platform.api.dto;

/**
 * 业务总览统计
 */
public class BusinessOverviewDto {
    private Long totalBusinesses;       // 总业务数
    private Long runningBusinesses;     // 运行中的业务数
    private Long activeProducts;        // 激活的产品总数
    private Long monthlyNewProducts;    // 本月新增产品总数
    
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
    
    public Long getActiveProducts() {
        return activeProducts;
    }
    
    public BusinessOverviewDto setActiveProducts(Long activeProducts) {
        this.activeProducts = activeProducts;
        return this;
    }
    
    public Long getMonthlyNewProducts() {
        return monthlyNewProducts;
    }
    
    public BusinessOverviewDto setMonthlyNewProducts(Long monthlyNewProducts) {
        this.monthlyNewProducts = monthlyNewProducts;
        return this;
    }
}
