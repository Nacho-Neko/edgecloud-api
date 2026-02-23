package com.mikou.edgecloud.edge.api.dto;

import java.util.UUID;

/**
 * 创建节点传输路径请求
 * 调用方先通过 GET /protocols?edgeTag=xxx 拿到目标节点的监听器列表，再用 targetProtocolId 创建传输。
 */
public class CreateEdgeTransferRequest {
    private UUID senderEdgeTag;
    private Integer sendIpId;
    private Integer targetProtocolId;
    private String hostName;
    private String remark;

    public UUID getSenderEdgeTag() { return senderEdgeTag; }
    public CreateEdgeTransferRequest setSenderEdgeTag(UUID senderEdgeTag) { this.senderEdgeTag = senderEdgeTag; return this; }

    public Integer getSendIpId() { return sendIpId; }
    public CreateEdgeTransferRequest setSendIpId(Integer sendIpId) { this.sendIpId = sendIpId; return this; }

    public Integer getTargetProtocolId() { return targetProtocolId; }
    public CreateEdgeTransferRequest setTargetProtocolId(Integer targetProtocolId) { this.targetProtocolId = targetProtocolId; return this; }

    public String getHostName() { return hostName; }
    public CreateEdgeTransferRequest setHostName(String hostName) { this.hostName = hostName; return this; }

    public String getRemark() { return remark; }
    public CreateEdgeTransferRequest setRemark(String remark) { this.remark = remark; return this; }
}