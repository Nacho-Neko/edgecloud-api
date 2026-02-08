package com.mikou.edgecloud.eop.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * EopBound 额外的 JSON 参数映射类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EopBoundParams {

    /**
     * 端口号 - 仅用于 Inbound
     */
    @JsonProperty("Port")
    private Integer port;

    @JsonProperty("OutTag")
    private UUID outTag;

    /**
     * 主机信息（IP和端口）- 仅用于 Outbound 回源
     * Key: IP地址, Value: 端口号
     */
    @JsonProperty("Host")
    private Map<String, Integer> host;

    /**
     * 传输路由
     */
    @JsonProperty("TransferRoute")
    private List<Integer> transferRoute;

    /**
     * 其他参数
     */
    @JsonProperty("Parameters")
    private Map<String, String> parameters;

    public Integer getPort() {
        return port;
    }

    public EopBoundParams setPort(Integer port) {
        this.port = port;
        return this;
    }

    public UUID getOutTag() {
        return outTag;
    }

    public EopBoundParams setOutTag(UUID outTag) {
        this.outTag = outTag;
        return this;
    }

    public Map<String, Integer> getHost() {
        return host;
    }

    public EopBoundParams setHost(Map<String, Integer> host) {
        this.host = host;
        return this;
    }

    public List<Integer> getTransferRoute() {
        return transferRoute;
    }

    public EopBoundParams setTransferRoute(List<Integer> transferRoute) {
        this.transferRoute = transferRoute;
        return this;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public EopBoundParams setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }
}
