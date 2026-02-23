package com.mikou.edgecloud.edge.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.Instant;
import java.util.UUID;

/**
 * 节点间传输路径实体
 * senderEdge 作为客户端，连接到 targetEdge 的某个 EdgeProtocol 监听器。
 * 对应表: edge_transfer
 */
@TableName("edge_transfer")
public class EdgeTransferEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 传输对唯一标识，对外暴露此字段作为操作句柄 */
    @TableField("pair_key")
    private UUID pairKey;

    /** 发送端 Edge 节点 tag */
    @TableField("sender_edge_tag")
    private UUID senderEdgeTag;

    /** 发送端绑定的 edge_nic_ip.id（出站 IP） */
    @TableField("send_ip_id")
    private Integer sendIpId;

    /**
     * 目标端 Edge 节点 tag（冗余存储，便于按节点查询，避免 JOIN edge_protocol）
     */
    @TableField("target_edge_tag")
    private UUID targetEdgeTag;

    /** 目标端监听器 edge_protocol.id */
    @TableField("target_protocol_id")
    private Integer targetProtocolId;

    @TableField("host_name")
    private String hostName;

    @TableField("remark")
    private String remark;

    @TableField("created_at")
    private Instant createdAt;

    @TableField("updated_at")
    private Instant updatedAt;

    @TableField("removed_at")
    private Instant removedAt;

    public EdgeTransferEntity() {}

    public Integer getId() { return id; }
    public EdgeTransferEntity setId(Integer id) { this.id = id; return this; }

    public UUID getPairKey() { return pairKey; }
    public EdgeTransferEntity setPairKey(UUID pairKey) { this.pairKey = pairKey; return this; }

    public UUID getSenderEdgeTag() { return senderEdgeTag; }
    public EdgeTransferEntity setSenderEdgeTag(UUID senderEdgeTag) { this.senderEdgeTag = senderEdgeTag; return this; }

    public Integer getSendIpId() { return sendIpId; }
    public EdgeTransferEntity setSendIpId(Integer sendIpId) { this.sendIpId = sendIpId; return this; }

    public UUID getTargetEdgeTag() { return targetEdgeTag; }
    public EdgeTransferEntity setTargetEdgeTag(UUID targetEdgeTag) { this.targetEdgeTag = targetEdgeTag; return this; }

    public Integer getTargetProtocolId() { return targetProtocolId; }
    public EdgeTransferEntity setTargetProtocolId(Integer targetProtocolId) { this.targetProtocolId = targetProtocolId; return this; }

    public String getHostName() { return hostName; }
    public EdgeTransferEntity setHostName(String hostName) { this.hostName = hostName; return this; }

    public String getRemark() { return remark; }
    public EdgeTransferEntity setRemark(String remark) { this.remark = remark; return this; }

    public Instant getCreatedAt() { return createdAt; }
    public EdgeTransferEntity setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }

    public Instant getUpdatedAt() { return updatedAt; }
    public EdgeTransferEntity setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

    public Instant getRemovedAt() { return removedAt; }
    public EdgeTransferEntity setRemovedAt(Instant removedAt) { this.removedAt = removedAt; return this; }
}