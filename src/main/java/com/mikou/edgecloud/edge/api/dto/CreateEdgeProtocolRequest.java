package com.mikou.edgecloud.edge.api.dto;

import java.util.UUID;

/**
 * 创建 Edge 节点监听器请求
 * port 与 portRange 互斥，具体取决于 protocol：
 *   QUIC → 仅允许 port（单端口）
 *   未来支持其他协议时 portRange 格式为 "start-end"，如 "10000-20000"
 */
public class CreateEdgeProtocolRequest {
    private UUID edgeTag;
    private String protocol;
    private Integer ipId;

    /** 单端口（与 portRange 互斥） */
    private Integer port;

    /** 端口范围，格式 "start-end"，如 "10000-20000"（与 port 互斥） */
    private String portRange;

    public UUID getEdgeTag() { return edgeTag; }
    public CreateEdgeProtocolRequest setEdgeTag(UUID edgeTag) { this.edgeTag = edgeTag; return this; }

    public String getProtocol() { return protocol; }
    public CreateEdgeProtocolRequest setProtocol(String protocol) { this.protocol = protocol; return this; }

    public Integer getIpId() { return ipId; }
    public CreateEdgeProtocolRequest setIpId(Integer ipId) { this.ipId = ipId; return this; }

    public Integer getPort() { return port; }
    public CreateEdgeProtocolRequest setPort(Integer port) { this.port = port; return this; }

    public String getPortRange() { return portRange; }
    public CreateEdgeProtocolRequest setPortRange(String portRange) { this.portRange = portRange; return this; }
}