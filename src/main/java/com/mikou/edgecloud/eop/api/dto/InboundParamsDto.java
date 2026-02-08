package com.mikou.edgecloud.eop.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InboundParamsDto {

    @JsonProperty("Port")
    @NotNull(message = "端口号 不能为空")
    private Integer port;

    @JsonProperty("OutTag")
    @NotNull(message = "出站Tag 不能为空")
    private UUID outTag;

    @JsonProperty("TransferRoute")
    @NotEmpty(message = "传输路由 不能为空")
    private List<Integer> transferRoute;

    @JsonProperty("Parameters")
    private Map<String, String> parameters;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public UUID getOutTag() {
        return outTag;
    }

    public void setOutTag(UUID outTag) {
        this.outTag = outTag;
    }

    public List<Integer> getTransferRoute() {
        return transferRoute;
    }

    public void setTransferRoute(List<Integer> transferRoute) {
        this.transferRoute = transferRoute;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
