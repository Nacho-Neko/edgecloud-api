package com.mikou.edgecloud.eop.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import java.util.Map;

public class OutboundParamsDto {

    @JsonProperty("Host")
    @NotEmpty(message = "回源主机 不能为空")
    private Map<String, Integer> host;

    @JsonProperty("Parameters")
    private Map<String, String> parameters;

    public Map<String, Integer> getHost() {
        return host;
    }

    public void setHost(Map<String, Integer> host) {
        this.host = host;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
