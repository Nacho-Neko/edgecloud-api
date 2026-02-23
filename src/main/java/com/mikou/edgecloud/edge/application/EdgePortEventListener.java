package com.mikou.edgecloud.edge.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.business.eop.domain.events.EopPortOccupyRequestedEvent;
import com.mikou.edgecloud.business.eop.domain.events.EopPortReleaseRequestedEvent;
import com.mikou.edgecloud.edge.domain.enums.IpPortOccupierType;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeIpPortOccupationEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeIpPortOccupationMapper;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Edge 层端口事件监听器
 * 监听 EOP（及其他业务域）发布的端口占用/释放事件，统一操作 edge_ip_port_occupation 表。
 */
@Component
public class EdgePortEventListener {

    private final EdgeIpPortOccupationMapper portOccupationMapper;

    public EdgePortEventListener(EdgeIpPortOccupationMapper portOccupationMapper) {
        this.portOccupationMapper = portOccupationMapper;
    }

    @EventListener
    @Transactional
    public void handleOccupy(EopPortOccupyRequestedEvent event) {
        // 校验端口未被占用
        Long count = portOccupationMapper.selectCount(
                new LambdaQueryWrapper<EdgeIpPortOccupationEntity>()
                        .eq(EdgeIpPortOccupationEntity::getIpId, event.ipId())
                        .eq(EdgeIpPortOccupationEntity::getPort, event.port()));
        if (count > 0) {
            throw new IllegalStateException(
                    String.format("Port %d on IP %d is already occupied", event.port(), event.ipId()));
        }

        IpPortOccupierType occupierType = IpPortOccupierType.valueOf(event.occupierType());

        portOccupationMapper.insert(new EdgeIpPortOccupationEntity()
                .setIpId(event.ipId())
                .setPort(event.port())
                .setOccupierType(occupierType)
                .setOccupierId(event.occupierId())
                .setCreatedAt(Instant.now()));
    }

    @EventListener
    @Transactional
    public void handleRelease(EopPortReleaseRequestedEvent event) {
        IpPortOccupierType occupierType = IpPortOccupierType.valueOf(event.occupierType());

        portOccupationMapper.delete(new LambdaQueryWrapper<EdgeIpPortOccupationEntity>()
                .eq(EdgeIpPortOccupationEntity::getOccupierType, occupierType)
                .eq(EdgeIpPortOccupationEntity::getOccupierId, event.occupierId()));
    }
}
