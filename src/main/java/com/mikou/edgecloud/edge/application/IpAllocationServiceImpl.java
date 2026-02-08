package com.mikou.edgecloud.edge.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.edge.domain.service.IpAllocationService;
import com.mikou.edgecloud.edge.domain.enums.IpStatus;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicIpEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeNicIpMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class IpAllocationServiceImpl implements IpAllocationService {

    private final EdgeNicIpMapper ipMapper;

    public IpAllocationServiceImpl(EdgeNicIpMapper ipMapper) {
        this.ipMapper = ipMapper;
    }

    @Override
    @Transactional
    public boolean allocateIp(Integer ipId, String allocationType, UUID allocationId) {
        EdgeNicIpEntity ip = ipMapper.selectById(ipId);
        if (ip == null) {
            throw new IllegalArgumentException("IP not found: " + ipId);
        }

        // 检查是否已分配（通过 allocatedToId 判断）
        if (ip.getAllocatedToId() != null) {
            throw new IllegalStateException("IP already allocated to: " +
                    ip.getAllocatedToType() + ":" + ip.getAllocatedToId());
        }

        // 只设置分配相关字段，不修改 status
        ip.setAllocatedToType(allocationType)
                .setAllocatedToId(allocationId)
                .setAllocatedAt(Instant.now())
                .setUpdatedAt(Instant.now());

        return ipMapper.updateById(ip) > 0;
    }

    @Override
    @Transactional
    public boolean releaseIp(Integer ipId) {
        EdgeNicIpEntity ip = ipMapper.selectById(ipId);
        if (ip == null) {
            return false;
        }

        // 只清空分配相关字段，不修改 status
        ip.setAllocatedToType(null)
                .setAllocatedToId(null)
                .setAllocatedAt(null)
                .setUpdatedAt(Instant.now());

        return ipMapper.updateById(ip) > 0;
    }

    @Override
    public List<EdgeNicIpEntity> findAvailableIps() {
        return ipMapper.selectList(
                new LambdaQueryWrapper<EdgeNicIpEntity>()
                        .isNull(EdgeNicIpEntity::getAllocatedToId)  // 未分配
                        .eq(EdgeNicIpEntity::getStatus, IpStatus.ACTIVE)  // 且状态为 ACTIVE
                        .isNull(EdgeNicIpEntity::getRemovedAt)
        );
    }

    @Override
    public List<EdgeNicIpEntity> findAllocatedIps(String allocationType, UUID allocationId) {
        return ipMapper.selectList(
                new LambdaQueryWrapper<EdgeNicIpEntity>()
                        .eq(EdgeNicIpEntity::getAllocatedToType, allocationType)
                        .eq(EdgeNicIpEntity::getAllocatedToId, allocationId)
                        .isNull(EdgeNicIpEntity::getRemovedAt)
        );
    }

    @Override
    public boolean isIpAllocated(Integer ipId) {
        EdgeNicIpEntity ip = ipMapper.selectById(ipId);
        return ip != null && ip.getAllocatedToId() != null;  // 通过 allocatedToId 判断
    }
}