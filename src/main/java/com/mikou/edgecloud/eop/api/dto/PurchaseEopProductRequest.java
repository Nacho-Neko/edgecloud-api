package com.mikou.edgecloud.eop.api.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public class PurchaseEopProductRequest {
    @NotNull(message = "productTag 不能为空")
    private UUID productTag;
    @NotNull(message = "ownerId 不能为空")
    private UUID ownerId;
    @Min(value = 1, message = "购买时长至少为 1 个月")
    private int durationMonths;

    public UUID getProductTag() {
        return productTag;
    }

    public void setProductTag(UUID productTag) {
        this.productTag = productTag;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }
}
