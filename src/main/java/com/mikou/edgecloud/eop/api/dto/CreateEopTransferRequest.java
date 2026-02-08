package com.mikou.edgecloud.eop.api.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class CreateEopTransferRequest {
    @NotBlank(message = "senderEopTag 不能为空")
    private String senderEopTag;
    @NotBlank(message = "targetEopTag 不能为空")
    private String targetEopTag;
    @NotNull(message = "sendIpId 不能为空")
    private Integer sendIpId;
    @NotNull(message = "receiveIpId 不能为空")
    private Integer receiveIpId;
    @NotBlank(message = "protocol 不能为空")
    private String protocol;
    @NotBlank(message = "hostName 不能为空")
    private String hostName;
    private String remark;

    public String getSenderEopTag() {
        return senderEopTag;
    }

    public void setSenderEopTag(String senderEopTag) {
        this.senderEopTag = senderEopTag;
    }

    public String getTargetEopTag() {
        return targetEopTag;
    }

    public void setTargetEopTag(String targetEopTag) {
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
}
