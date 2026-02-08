package com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.Instant;
import java.util.UUID;

/**
 * EOP 传输实体：源工作负载 (Source Workload) 到 目标工作负载 (Target Workload) 的传输配置
 */
@TableName("eop_transfer")
public class EopTransferEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("pair_key")
    private UUID pairKey;

    /**
     * 源端工作负载 ID (发送端)
     */
    @TableField("sender_eop")
    private UUID senderEop;

    /**
     * 目标端工作负载 ID (接收端)
     */
    @TableField("target_eop")
    private UUID targetEop;

    /**
     * 发送端 IP ID
     */
    @TableField("send_ip_id")
    private Integer sendIpId;

    /**
     * 接收端 IP ID
     */
    @TableField("receive_ip_id")
    private Integer receiveIpId;

    /**
     * 主机名
     */
    @TableField("host_name")
    private String hostName;

    /**
     * 协议
     */
    @TableField("protocol")
    private String protocol;

    @TableField("remark")
    private String remark;

    @TableField("created_at")
    private Instant createdAt;

    @TableField("updated_at")
    private Instant updatedAt;

    @TableField("removed_at")
    private Instant removedAt;

    public EopTransferEntity() {

    }


    public Integer getId() {
        return id;
    }

    public EopTransferEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public UUID getPairKey() {
        return pairKey;
    }

    public EopTransferEntity setPairKey(UUID pairKey) {
        this.pairKey = pairKey;
        return this;
    }

    public UUID getSenderEop() {
        return senderEop;
    }

    public EopTransferEntity setSenderEop(UUID senderEop) {
        this.senderEop = senderEop;
        return this;
    }

    public UUID getTargetEop() {
        return targetEop;
    }

    public EopTransferEntity setTargetEop(UUID targetEop) {
        this.targetEop = targetEop;
        return this;
    }

    public Integer getSendIpId() {
        return sendIpId;
    }

    public EopTransferEntity setSendIpId(Integer sendIpId) {
        this.sendIpId = sendIpId;
        return this;
    }

    public Integer getReceiveIpId() {
        return receiveIpId;
    }

    public EopTransferEntity setReceiveIpId(Integer receiveIpId) {
        this.receiveIpId = receiveIpId;
        return this;
    }

    public String getHostName() {
        return hostName;
    }

    public EopTransferEntity setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public EopTransferEntity setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public EopTransferEntity setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public EopTransferEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public EopTransferEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Instant getRemovedAt() {
        return removedAt;
    }

    public EopTransferEntity setRemovedAt(Instant removedAt) {
        this.removedAt = removedAt;
        return this;
    }
}
