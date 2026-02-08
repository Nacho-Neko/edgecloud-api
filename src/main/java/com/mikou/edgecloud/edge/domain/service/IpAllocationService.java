package com.mikou.edgecloud.edge.domain.service;

import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicIpEntity;

import java.util.UUID;

/**
 * IP 分配领域服务
 * 提供 IP 资源的分配、释放、查询等核心能力
 */
public interface IpAllocationService {

    /**
     * 分配 IP 给指定资源
     * @param ipId IP 的 ID
     * @param allocationType 分配目标类型（如 "EOP", "CDN"）
     * @param allocationId 分配目标的 ID
     * @return 是否分配成功
     */
    boolean allocateIp(Integer ipId, String allocationType, UUID allocationId);

    /**
     * 释放 IP 分配
     * @param ipId IP 的 ID
     * @return 是否释放成功
     */
    boolean releaseIp(Integer ipId);

    /**
     * 查询某个资源分配的所有 IP
     * @param allocationType 资源类型
     * @param allocationId 资源 Tag
     * @return IP 列表
     */
    java.util.List<EdgeNicIpEntity> findAllocatedIps(String allocationType, UUID allocationId);

    /**
     * 查询可用的（未分配的）IP
     * @return 可用 IP 列表
     */
    java.util.List<EdgeNicIpEntity> findAvailableIps();

    /**
     * 检查 IP 是否已分配
     * @param ipId IP 的 ID
     * @return 是否已分配
     */
    boolean isIpAllocated(Integer ipId);
}