package com.mikou.edgecloud.eop.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.eop.domain.events.PortApplyRequestedEvent;
import com.mikou.edgecloud.eop.domain.events.PortOccupiedEvent;
import com.mikou.edgecloud.eop.domain.events.PortReleaseRequestedEvent;
import com.mikou.edgecloud.eop.domain.enums.EopOccupierType;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.EopPortOccupationEntity;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopPortOccupationMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EopPortService {

    private final EopPortOccupationMapper portOccupationMapper;
    private final ApplicationEventPublisher eventPublisher;

    public EopPortService(EopPortOccupationMapper portOccupationMapper, ApplicationEventPublisher eventPublisher) {
        this.portOccupationMapper = portOccupationMapper;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 发起端口申请请求 (通过领域事件)
     */
    public void applyPort(Integer ipId, Integer port, EopOccupierType occupierType, Integer occupierId) {
        eventPublisher.publishEvent(new PortApplyRequestedEvent(ipId, port, occupierType, occupierId));
    }

    /**
     * 发起端口释放请求 (通过领域事件)
     */
    public void requestReleasePort(Integer ipId, Integer port) {
        eventPublisher.publishEvent(PortReleaseRequestedEvent.single(ipId, port));
    }

    /**
     * 发起释放某个占用者的所有端口请求 (通过领域事件)
     */
    public void requestReleaseAllPortsByOccupier(EopOccupierType occupierType, Integer occupierId) {
        eventPublisher.publishEvent(PortReleaseRequestedEvent.allByOccupier(occupierType, occupierId));
    }

    /**
     * 校验端口是否可用
     */
    public boolean isPortAvailable(Integer ipId, Integer port) {
        Long count = portOccupationMapper.selectCount(new LambdaQueryWrapper<EopPortOccupationEntity>()
                .eq(EopPortOccupationEntity::getIpId, ipId)
                .eq(EopPortOccupationEntity::getPort, port));
        return count == 0;
    }

    /**
     * 获取指定 IP 在指定范围内的可用端口列表
     */
    public List<Integer> findAvailablePorts(Integer ipId, int startPort, int endPort, int limit) {
        List<EopPortOccupationEntity> occupations = portOccupationMapper.selectList(new LambdaQueryWrapper<EopPortOccupationEntity>()
                .eq(EopPortOccupationEntity::getIpId, ipId)
                .between(EopPortOccupationEntity::getPort, startPort, endPort));

        Set<Integer> occupiedPorts = occupations.stream()
                .map(EopPortOccupationEntity::getPort)
                .collect(Collectors.toSet());

        List<Integer> availablePorts = new ArrayList<>();
        for (int port = startPort; port <= endPort && availablePorts.size() < limit; port++) {
            if (!occupiedPorts.contains(port)) {
                availablePorts.add(port);
            }
        }
        return availablePorts;
    }

    /**
     * 执行实际的占用操作 (通常由事件监听器调用)
     */
    @Transactional
    public void executeOccupyPort(Integer ipId, Integer port, EopOccupierType occupierType, Integer occupierId) {
        if (!isPortAvailable(ipId, port)) {
            throw new IllegalStateException(String.format("Port %d on IP %d is already occupied", port, ipId));
        }

        EopPortOccupationEntity entity = new EopPortOccupationEntity()
                .setIpId(ipId)
                .setPort(port)
                .setOccupierType(occupierType)
                .setOccupierId(occupierId)
                .setCreatedAt(Instant.now());

        portOccupationMapper.insert(entity);
        
        // 发送占用成功确认事件
        eventPublisher.publishEvent(new PortOccupiedEvent(ipId, port, occupierType, occupierId));
    }

    /**
     * 执行实际的释放操作 (通常由事件监听器调用)
     */
    @Transactional
    public void executeReleasePort(Integer ipId, Integer port) {
        portOccupationMapper.delete(new LambdaQueryWrapper<EopPortOccupationEntity>()
                .eq(EopPortOccupationEntity::getIpId, ipId)
                .eq(EopPortOccupationEntity::getPort, port));
    }

    /**
     * 执行实际的批量释放操作 (通常由事件监听器调用)
     */
    @Transactional
    public void executeReleaseAllPortsByOccupier(EopOccupierType occupierType, Integer occupierId) {
        portOccupationMapper.delete(new LambdaQueryWrapper<EopPortOccupationEntity>()
                .eq(EopPortOccupationEntity::getOccupierType, occupierType)
                .eq(EopPortOccupationEntity::getOccupierId, occupierId));
    }
}
