package com.mikou.edgecloud.eop.api.dto;

import java.time.Instant;
import java.util.UUID;

public class EopTransferDto {
    private Integer id;
    private UUID pairKey;
    private UUID senderEopTag;
    private UUID targetEopTag;
    private Integer sendIpId;
    private Integer receiveIpId;
    private String sendIp;
    private String receiveIp;
    private String protocol;
    private String hostName;
    private String remark;
    private Instant createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getPairKey() {
        return pairKey;
    }

    public void setPairKey(UUID pairKey) {
        this.pairKey = pairKey;
    }

    public UUID getSenderEopTag() {
        return senderEopTag;
    }

    public void setSenderEopTag(UUID senderEopTag) {
        this.senderEopTag = senderEopTag;
    }

    public UUID getTargetEopTag() {
        return targetEopTag;
    }

    public void setTargetEopTag(UUID targetEopTag) {
        this.targetEopTag = targetEopTag;
    }

    public Integer getSendIpId() {
        return sendIpId;
    }

    public void setSendIpId(Integer sendIpId) {
        this.sendIpId = sendIpId;
    }

    public Integer getReceiveIpId() {
        return receiveIpId;
    }

    public void setReceiveIpId(Integer receiveIpId) {
        this.receiveIpId = receiveIpId;
    }

    public String getSendIp() {
        return sendIp;
    }

    public void setSendIp(String sendIp) {
        this.sendIp = sendIp;
    }

    public String getReceiveIp() {
        return receiveIp;
    }

    public void setReceiveIp(String receiveIp) {
        this.receiveIp = receiveIp;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
