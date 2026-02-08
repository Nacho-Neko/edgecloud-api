
package com.mikou.edgecloud.edge.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mikou.edgecloud.edge.domain.enums.IpStatus;
import java.net.InetAddress;
import java.time.Instant;
import java.util.UUID;

@TableName("edge_nic_ip")
public class EdgeNicIpEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("nic_id")
    private Integer nicId;

    @TableField("private_ip")
    private String privateIp;

    @TableField("public_ip")
    private String publicIp;

    @TableField("status")
    private IpStatus status;

    // 分配关系字段
    @TableField("allocated_to_type")
    private String allocatedToType;  // 例如: "EOP", "CDN", "EDGE", "LOAD_BALANCER" 等

    @TableField("allocated_to_id")
    private UUID allocatedToId;    // 目标资源的ID（字符串类型以支持各种ID格式）

    @TableField("allocated_at")
    private Instant allocatedAt;     // 分配时间

    @TableField("created_at")
    private Instant createdAt;

    @TableField("updated_at")
    private Instant updatedAt;

    @TableField("removed_at")
    private Instant removedAt;

    public Integer getId() { return id; }
    public EdgeNicIpEntity setId(Integer id) { this.id = id; return this; }
    public Integer getNicId() { return nicId; }
    public EdgeNicIpEntity setNicId(Integer nicId) { this.nicId = nicId; return this; }
    public String getPrivateIp() { return privateIp; }
    public EdgeNicIpEntity setPrivateIp(String privateIp) {
        if (privateIp == null || privateIp.isBlank()) {
            throw new IllegalArgumentException("privateIp cannot be null or empty");
        }
        validateIp(privateIp);
        this.privateIp = privateIp;
        return this;
    }

    /**
     * 以 InetAddress 形式获取 privateIp
     */
    public InetAddress getPrivateIpAddress() {
        return parseIp(privateIp);
    }

    public EdgeNicIpEntity setPrivateIpAddress(InetAddress address) {
        if (address == null) {
            throw new IllegalArgumentException("privateIp address cannot be null");
        }
        this.privateIp = address.getHostAddress();
        return this;
    }

    public String getPublicIp() { return publicIp; }
    public EdgeNicIpEntity setPublicIp(String publicIp) {
        if (publicIp != null && !publicIp.isBlank()) {
            validateIp(publicIp);
        }
        this.publicIp = publicIp;
        return this;
    }

    /**
     * 以 InetAddress 形式获取 publicIp
     */
    public InetAddress getPublicIpAddress() {
        return parseIp(publicIp);
    }

    public EdgeNicIpEntity setPublicIpAddress(InetAddress address) {
        this.publicIp = address != null ? address.getHostAddress() : null;
        return this;
    }
    public IpStatus getStatus() { return status; }
    public EdgeNicIpEntity setStatus(IpStatus status) { this.status = status; return this; }

    public String getAllocatedToType() { return allocatedToType; }
    public EdgeNicIpEntity setAllocatedToType(String allocatedToType) { this.allocatedToType = allocatedToType; return this; }
    public UUID getAllocatedToId() { return allocatedToId; }
    public EdgeNicIpEntity setAllocatedToId(UUID allocatedToId) { this.allocatedToId = allocatedToId; return this; }
    public Instant getAllocatedAt() { return allocatedAt; }
    public EdgeNicIpEntity setAllocatedAt(Instant allocatedAt) { this.allocatedAt = allocatedAt; return this; }

    public Instant getCreatedAt() { return createdAt; }
    public EdgeNicIpEntity setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }
    public Instant getUpdatedAt() { return updatedAt; }
    public EdgeNicIpEntity setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }
    public Instant getRemovedAt() { return removedAt; }
    public EdgeNicIpEntity setRemovedAt(Instant removedAt) { this.removedAt = removedAt; return this; }

    /**
     * 内部校验逻辑：效仿 C# IPAddress 解析
     */
    private void validateIp(String ip) {
        if (ip == null || ip.isBlank()) return;
        try {
            // InetAddress.getByName 会验证 IP 格式
            InetAddress.getByName(ip);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid IP address format: " + ip);
        }
    }

    private InetAddress parseIp(String ip) {
        if (ip == null || ip.isBlank()) return null;
        try {
            return InetAddress.getByName(ip);
        } catch (Exception e) {
            return null;
        }
    }
}