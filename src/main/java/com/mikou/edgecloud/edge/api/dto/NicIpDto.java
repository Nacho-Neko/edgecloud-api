package com.mikou.edgecloud.edge.api.dto;

import com.mikou.edgecloud.edge.domain.enums.IpStatus;

import java.util.UUID;

public class NicIpDto {
    private Integer id;
    private String privateIp;
    private String publicIp;
    private IpStatus status;// ACTIVE/REMOVED
    private boolean allocated;       // 是否已分配
    private String allocatedToType;  // 分配给谁（类型）
    private UUID allocatedToId;    // 分配给谁（ID）

    public Integer getId() {
        return id;
    }

    public NicIpDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getPrivateIp() {
        return privateIp;
    }

    public NicIpDto setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
        return this;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public NicIpDto setPublicIp(String publicIp) {
        this.publicIp = publicIp;
        return this;
    }

    public IpStatus getStatus() {
        return status;
    }

    public NicIpDto setStatus(IpStatus status) {
        this.status = status;
        return this;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public NicIpDto setAllocated(boolean allocated) {
        this.allocated = allocated;
        return this;
    }

    public String getAllocatedToType() {
        return allocatedToType;
    }

    public NicIpDto setAllocatedToType(String allocatedToType) {
        this.allocatedToType = allocatedToType;
        return this;
    }

    public UUID getAllocatedToId() {
        return allocatedToId;
    }

    public NicIpDto setAllocatedToId(UUID allocatedToId) {
        this.allocatedToId = allocatedToId;
        return this;
    }
}