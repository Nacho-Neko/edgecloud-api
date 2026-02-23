package com.mikou.edgecloud.edge.api.dto;

import java.util.UUID;

/**
 * 创建 Edge 节点监听器请求
 */
public class CreateEdgeProtocolRequest {
    private UUID edgeTag;
    private String protocol;
    private Integer ipId;
    private Integer port;
    private Integer portRangeStart;
    private Integer portRangeEnd;

    public UUID getEdgeTag() { return edgeTag; }
    public CreateEdgeProtocolRequest setEdgeTag(UUID edgeTag) { this.edgeTag = edgeTag; return this; }

    public String getProtocol() { return protocol; }
    public CreateEdgeProtocolRequest setProtocol(String protocol) { this.protocol = protocol; return this; }

    public Integer getIpId() { return ipId; }
    public CreateEdgeProtocolRequest setIpId(Integer ipId) { this.ipId = ipId; return this; }

    public Integer getPort() { return port; }
    public CreateEdgeProtocolRequest setPort(Integer port) { this.port = port; return this; }

    public Integer getPortRangeStart() { return portRangeStart; }
    public CreateEdgeProtocolRequest setPortRangeStart(Integer portRangeStart) { this.portRangeStart = portRangeStart; return this; }

    public Integer getPortRangeEnd() { return portRangeEnd; }
    public CreateEdgeProtocolRequest setPortRangeEnd(Integer portRangeEnd) { this.portRangeEnd = portRangeEnd; return this; }
}
