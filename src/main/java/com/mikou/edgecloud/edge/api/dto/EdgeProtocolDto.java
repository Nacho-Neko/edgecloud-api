package com.mikou.edgecloud.edge.api.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Edge 节点监听器配置 DTO
 */
public class EdgeProtocolDto {
    private Integer id;
    private UUID edgeTag;
    private String protocol;
    private Integer ipId;
    private String ip;              // 解析自 edge_nic_ip
    private Integer port;
    private Integer portRangeStart;
    private Integer portRangeEnd;
    private Instant createdAt;

    public Integer getId() { return id; }
    public EdgeProtocolDto setId(Integer id) { this.id = id; return this; }

    public UUID getEdgeTag() { return edgeTag; }
    public EdgeProtocolDto setEdgeTag(UUID edgeTag) { this.edgeTag = edgeTag; return this; }

    public String getProtocol() { return protocol; }
    public EdgeProtocolDto setProtocol(String protocol) { this.protocol = protocol; return this; }

    public Integer getIpId() { return ipId; }
    public EdgeProtocolDto setIpId(Integer ipId) { this.ipId = ipId; return this; }

    public String getIp() { return ip; }
    public EdgeProtocolDto setIp(String ip) { this.ip = ip; return this; }

    public Integer getPort() { return port; }
    public EdgeProtocolDto setPort(Integer port) { this.port = port; return this; }

    public Integer getPortRangeStart() { return portRangeStart; }
    public EdgeProtocolDto setPortRangeStart(Integer portRangeStart) { this.portRangeStart = portRangeStart; return this; }

    public Integer getPortRangeEnd() { return portRangeEnd; }
    public EdgeProtocolDto setPortRangeEnd(Integer portRangeEnd) { this.portRangeEnd = portRangeEnd; return this; }

    public Instant getCreatedAt() { return createdAt; }
    public EdgeProtocolDto setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }
}
