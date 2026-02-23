package com.mikou.edgecloud.edge.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.Instant;
import java.util.UUID;

/**
 * Edge 节点传输监听器配置
 * 一个节点可以在某个 IP:Port 上监听特定协议，供其他节点的 EdgeTransfer 连入。
 */
@TableName("edge_protocol")
public class EdgeProtocolEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 所属 Edge 节点 tag */
    @TableField("edge_tag")
    private UUID edgeTag;

    /** 协议名称，如 TCP、UDP */
    @TableField("protocol")
    private String protocol;

    /** 监听的 edge_nic_ip.id */
    @TableField("ip_id")
    private Integer ipId;

    /** 单端口监听 */
    @TableField("port")
    private Integer port;

    /** 端口范围监听（起始，与 port 二选一） */
    @TableField("port_range_start")
    private Integer portRangeStart;

    @TableField("port_range_end")
    private Integer portRangeEnd;

    @TableField("created_at")
    private Instant createdAt;

    @TableField("updated_at")
    private Instant updatedAt;

    @TableField("removed_at")
    private Instant removedAt;

    public EdgeProtocolEntity() {}

    public Integer getId() { return id; }
    public EdgeProtocolEntity setId(Integer id) { this.id = id; return this; }

    public UUID getEdgeTag() { return edgeTag; }
    public EdgeProtocolEntity setEdgeTag(UUID edgeTag) { this.edgeTag = edgeTag; return this; }

    public String getProtocol() { return protocol; }
    public EdgeProtocolEntity setProtocol(String protocol) { this.protocol = protocol; return this; }

    public Integer getIpId() { return ipId; }
    public EdgeProtocolEntity setIpId(Integer ipId) { this.ipId = ipId; return this; }

    public Integer getPort() { return port; }
    public EdgeProtocolEntity setPort(Integer port) { this.port = port; return this; }

    public Integer getPortRangeStart() { return portRangeStart; }
    public EdgeProtocolEntity setPortRangeStart(Integer portRangeStart) { this.portRangeStart = portRangeStart; return this; }

    public Integer getPortRangeEnd() { return portRangeEnd; }
    public EdgeProtocolEntity setPortRangeEnd(Integer portRangeEnd) { this.portRangeEnd = portRangeEnd; return this; }

    public Instant getCreatedAt() { return createdAt; }
    public EdgeProtocolEntity setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }

    public Instant getUpdatedAt() { return updatedAt; }
    public EdgeProtocolEntity setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

    public Instant getRemovedAt() { return removedAt; }
    public EdgeProtocolEntity setRemovedAt(Instant removedAt) { this.removedAt = removedAt; return this; }
}
