package com.mikou.edgecloud.platform.api.dto;

/**
 * 业务详细信息
 */
public class BusinessDetailDto {
    private String code;                // 业务代码
    private String name;                // 业务名称
    private String status;              // 业务状态
    private Long activeProducts;        // 激活的产品数
    private Long monthlyNewProducts;    // 本月新增产品数
    
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
    
    public Long getActiveProducts() {
        return activeProducts;
    }
    
    public BusinessDetailDto setActiveProducts(Long activeProducts) {
        this.activeProducts = activeProducts;
        return this;
    }
    
    public Long getMonthlyNewProducts() {
        return monthlyNewProducts;
    }
    
    public BusinessDetailDto setMonthlyNewProducts(Long monthlyNewProducts) {
        this.monthlyNewProducts = monthlyNewProducts;
        return this;
    }
}
