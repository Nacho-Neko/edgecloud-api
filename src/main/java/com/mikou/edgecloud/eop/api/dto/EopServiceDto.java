package com.mikou.edgecloud.eop.api.dto;

import com.mikou.edgecloud.eop.domain.enums.EopServiceStatus;
import com.mikou.edgecloud.eop.domain.model.EopServiceEntitlements;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class EopServiceDto {
    private UUID tag;
    private String productName;
    private UUID productTag;
    private BigDecimal monthlyPrice;
    private EopServiceStatus status;
    private EopServiceEntitlements entitlements;
    private Instant expiredAt;
    private Instant createdAt;

    public UUID getTag() { return tag; }
    public EopServiceDto setTag(UUID tag) { this.tag = tag; return this; }

    public String getProductName() { return productName; }
    public EopServiceDto setProductName(String productName) { this.productName = productName; return this; }

    public UUID getProductTag() { return productTag; }
    public EopServiceDto setProductTag(UUID productTag) { this.productTag = productTag; return this; }

    public BigDecimal getMonthlyPrice() { return monthlyPrice; }
    public EopServiceDto setMonthlyPrice(BigDecimal monthlyPrice) { this.monthlyPrice = monthlyPrice; return this; }

    public EopServiceStatus getStatus() { return status; }
    public EopServiceDto setStatus(EopServiceStatus status) { this.status = status; return this; }

    public EopServiceEntitlements getEntitlements() { return entitlements; }
    public EopServiceDto setEntitlements(EopServiceEntitlements entitlements) { this.entitlements = entitlements; return this; }

    public Instant getExpiredAt() { return expiredAt; }
    public EopServiceDto setExpiredAt(Instant expiredAt) { this.expiredAt = expiredAt; return this; }

    public Instant getCreatedAt() { return createdAt; }
    public EopServiceDto setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }
}
