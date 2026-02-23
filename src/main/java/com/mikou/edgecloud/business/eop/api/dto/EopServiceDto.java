package com.mikou.edgecloud.business.eop.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mikou.edgecloud.business.api.dto.BusinessServiceDto;
import com.mikou.edgecloud.business.domain.ProductStatus;
import com.mikou.edgecloud.business.eop.domain.model.EopServiceEntitlements;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * EOP 业务服务 DTO
 * 继承 BusinessServiceDto 抽象类，保证字段名统一
 * 
 * 特有字段：
 * - entitlements: EOP 服务的权益信息
 */
public class EopServiceDto extends BusinessServiceDto {
    
    /**
     * EOP 服务的权益信息（EOP 特有）
     */
    private EopServiceEntitlements entitlements;

    public EopServiceDto() {
        // 设置业务类型为 EOP
        this.businessType = "EOP";
    }

    // ========== 便捷方法：兼容旧代码 ==========
    
    /**
     * @deprecated 使用 setServiceTag 代替，保持向后兼容
     */
    @Deprecated
    @JsonIgnore
    public UUID getEopTag() {
        return this.serviceTag;
    }

    /**
     * @deprecated 使用 setServiceTag 代替，保持向后兼容
     */
    @Deprecated
    public EopServiceDto setEopTag(UUID eopTag) {
        this.serviceTag = eopTag;
        return this;
    }

    // ========== 重写 setter 方法以支持链式调用 ==========
    
    @Override
    public EopServiceDto setServiceTag(UUID serviceTag) {
        this.serviceTag = serviceTag;
        return this;
    }

    @Override
    public EopServiceDto setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    @Override
    public EopServiceDto setProductTag(UUID productTag) {
        this.productTag = productTag;
        return this;
    }

    @Override
    public EopServiceDto setMonthlyPrice(BigDecimal monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
        return this;
    }

    @Override
    public EopServiceDto setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
        return this;
    }

    @Override
    public EopServiceDto setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
        return this;
    }

    @Override
    public EopServiceDto setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public EopServiceDto setAccount(com.mikou.edgecloud.business.api.dto.AccountSimpleDto account) {
        this.account = account;
        return this;
    }

    // ========== EOP 特有字段的 getter/setter ==========
    
    public EopServiceEntitlements getEntitlements() {
        return entitlements;
    }

    public EopServiceDto setEntitlements(EopServiceEntitlements entitlements) {
        this.entitlements = entitlements;
        return this;
    }
}
