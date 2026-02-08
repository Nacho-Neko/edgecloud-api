package com.mikou.edgecloud.eop.api.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateInboundRequest {
    @NotNull(message = "产品Tag 不能为空")
    private UUID serviceTag;
    @NotNull(message = "节点Tag 不能为空")
    private UUID eopTag;
    @NotNull(message = "监听地址 不能为空")
    private Integer addrId; // 不可空
    
    @Valid
    @NotNull(message = "extraParams 不能为空")
    private InboundParamsDto extraParams;

    public UUID getServiceTag() {
        return serviceTag;
    }

    public void setServiceTag(UUID serviceTag) {
        this.serviceTag = serviceTag;
    }

    public UUID getEopTag() {
        return eopTag;
    }

    public void setEopTag(UUID eopTag) {
        this.eopTag = eopTag;
    }

    public Integer getAddrId() {
        return addrId;
    }

    public void setAddrId(Integer addrId) {
        this.addrId = addrId;
    }

    public InboundParamsDto getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(InboundParamsDto extraParams) {
        this.extraParams = extraParams;
    }
}
