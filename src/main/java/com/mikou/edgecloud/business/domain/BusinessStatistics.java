package com.mikou.edgecloud.business.domain;

/**
 * 业务统计数据
 * 
 * 统计原则：只统计有意义的数据
 * - 当前激活的产品数量：反映业务规模
 * - 本月新增产品数量：反映业务增长
 * 
 * 不统计的：
 * - 暂停/过期数量：知道了也没用，不能强迫人家续费
 * - 总产品数：包含过期的没意义
 * - 用户数/收入：不是业务领域该管的
 */
public class BusinessStatistics {
    
    /**
     * 当前激活的产品数量（ACTIVE 状态）
     */
    private Long activeProducts;
    
    /**
     * 本月新增产品数量
     */
    private Long monthlyNewProducts;
    
    public Long getActiveProducts() {
        return activeProducts;
    }
    
    public BusinessStatistics setActiveProducts(Long activeProducts) {
        this.activeProducts = activeProducts;
        return this;
    }
    
    public Long getMonthlyNewProducts() {
        return monthlyNewProducts;
    }
    
    public BusinessStatistics setMonthlyNewProducts(Long monthlyNewProducts) {
        this.monthlyNewProducts = monthlyNewProducts;
        return this;
    }
}
