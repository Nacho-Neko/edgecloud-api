package com.mikou.edgecloud.eop.api.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateOutboundRequest {
    @NotNull(message = "serviceTag 不能为空")
    private UUID serviceTag;
    @NotNull(message = "eopTag 不能为空")
    private UUID eopTag;
    @NotNull(message = "addrId 不能为空")
    private Integer addrId; // 不可空
    
    @Valid
    @NotNull(message = "extraParams 不能为空")
    private OutboundParamsDto extraParams;

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

    public OutboundParamsDto getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(OutboundParamsDto extraParams) {
        this.extraParams = extraParams;
    }
}
