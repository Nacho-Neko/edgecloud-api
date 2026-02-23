package com.mikou.edgecloud.business.application.dto;

/**
 * 业务产品数量排名
 */
public class BusinessProductRank {
    private String businessCode;
    private String businessName;
    private Long totalProducts;
    private Long activeProducts;
    
    public String getBusinessCode() {
        return businessCode;
    }
    
    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }
    
    public String getBusinessName() {
        return businessName;
    }
    
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    
    public Long getTotalProducts() {
        return totalProducts;
    }
    
    public void setTotalProducts(Long totalProducts) {
        this.totalProducts = totalProducts;
    }
    
    public Long getActiveProducts() {
        return activeProducts;
    }
    
    public void setActiveProducts(Long activeProducts) {
        this.activeProducts = activeProducts;
    }
}
