package com.mikou.edgecloud.edge.api.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * 节点传输路径 DTO
 * pairKey 是对外操作句柄；targetProtocol 内含接收端节点、IP、端口信息。
 */
public class EdgeTransferDto {
    private UUID pairKey;
    private UUID senderEdgeTag;
    private Integer sendIpId;
    private String sendIp;
    private EdgeProtocolDto targetProtocol;  // 完整的目标监听器信息
    private String hostName;
    private String remark;
    private Instant createdAt;

    public UUID getPairKey() { return pairKey; }
    public EdgeTransferDto setPairKey(UUID pairKey) { this.pairKey = pairKey; return this; }

    public UUID getSenderEdgeTag() { return senderEdgeTag; }
    public EdgeTransferDto setSenderEdgeTag(UUID senderEdgeTag) { this.senderEdgeTag = senderEdgeTag; return this; }

    public Integer getSendIpId() { return sendIpId; }
    public EdgeTransferDto setSendIpId(Integer sendIpId) { this.sendIpId = sendIpId; return this; }

    public String getSendIp() { return sendIp; }
    public EdgeTransferDto setSendIp(String sendIp) { this.sendIp = sendIp; return this; }

    public EdgeProtocolDto getTargetProtocol() { return targetProtocol; }
    public EdgeTransferDto setTargetProtocol(EdgeProtocolDto targetProtocol) { this.targetProtocol = targetProtocol; return this; }

    public String getHostName() { return hostName; }
    public EdgeTransferDto setHostName(String hostName) { this.hostName = hostName; return this; }

    public String getRemark() { return remark; }
    public EdgeTransferDto setRemark(String remark) { this.remark = remark; return this; }

    public Instant getCreatedAt() { return createdAt; }
    public EdgeTransferDto setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }
}