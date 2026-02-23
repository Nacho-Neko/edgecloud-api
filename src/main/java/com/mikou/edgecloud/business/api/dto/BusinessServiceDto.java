package com.mikou.edgecloud.business.api.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mikou.edgecloud.business.domain.ProductStatus;
import com.mikou.edgecloud.business.eop.api.dto.EopServiceDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * 业务服务 DTO 抽象基类
 * 所有业务服务的 DTO 都应该继承此类，以保证返回结构的一致性
 * 
 * 使用抽象类而不是接口的原因：
 * 1. 可以定义统一的字段名，确保 JSON 序列化时字段一致
 * 2. 可以提供默认实现
 * 3. 子类可以添加自己特有的字段
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "businessType", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EopServiceDto.class, name = "EOP")
})
public abstract class BusinessServiceDto {
    
    /**
     * 业务类型：EOP, Edge, IoT 等
     */
    protected String businessType;
    
    /**
     * 业务服务的唯一标识
     */
    protected UUID serviceTag;
    
    /**
     * 产品名称
     */
    protected String productName;
    
    /**
     * 产品标识
     */
    protected UUID productTag;
    
    /**
     * 月度价格
     */
    protected BigDecimal monthlyPrice;
    
    /**
     * 服务状态
     */
    protected ProductStatus productStatus;
    
    /**
     * 过期时间
     */
    protected Instant expiredAt;
    
    /**
     * 创建时间
     */
    protected Instant createdAt;
    
    /**
     * 账户信息
     */
    protected AccountSimpleDto account;

    // ========== Getters and Setters ==========
    
    public String getBusinessType() {
        return businessType;
    }

    public BusinessServiceDto setBusinessType(String businessType) {
        this.businessType = businessType;
        return this;
    }

    public UUID getServiceTag() {
        return serviceTag;
    }

    public BusinessServiceDto setServiceTag(UUID serviceTag) {
        this.serviceTag = serviceTag;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public BusinessServiceDto setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public UUID getProductTag() {
        return productTag;
    }

    public BusinessServiceDto setProductTag(UUID productTag) {
        this.productTag = productTag;
        return this;
    }

    public BigDecimal getMonthlyPrice() {
        return monthlyPrice;
    }

    public BusinessServiceDto setMonthlyPrice(BigDecimal monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
        return this;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public BusinessServiceDto setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
        return this;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public BusinessServiceDto setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public BusinessServiceDto setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public AccountSimpleDto getAccount() {
        return account;
    }

    public BusinessServiceDto setAccount(AccountSimpleDto account) {
        this.account = account;
        return this;
    }
}
