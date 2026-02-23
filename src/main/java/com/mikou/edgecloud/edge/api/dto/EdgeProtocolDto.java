package com.mikou.edgecloud.edge.api.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Edge 节点监听器配置 DTO
 * port 与 portRange 互斥，由 protocol 类型决定哪个有值。
 */
public class EdgeProtocolDto {
    private Integer id;
    private UUID edgeTag;
    private String protocol;
    private Integer ipId;
    private String ip;

    /** 单端口（QUIC 使用） */
    private Integer port;

    /** 端口范围，格式 "start-end"（其他协议使用） */
    private String portRange;

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

    public String getPortRange() { return portRange; }
    public EdgeProtocolDto setPortRange(String portRange) { this.portRange = portRange; return this; }

    public Instant getCreatedAt() { return createdAt; }
    public EdgeProtocolDto setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }
}