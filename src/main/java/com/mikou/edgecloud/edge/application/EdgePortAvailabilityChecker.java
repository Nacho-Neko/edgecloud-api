package com.mikou.edgecloud.edge.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.common.spi.PortAvailabilityChecker;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeIpPortOccupationEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeIpPortOccupationMapper;
import org.springframework.stereotype.Component;

@Component
public class EdgePortAvailabilityChecker implements PortAvailabilityChecker {

    private final EdgeIpPortOccupationMapper portOccupationMapper;

    public EdgePortAvailabilityChecker(EdgeIpPortOccupationMapper portOccupationMapper) {
        this.portOccupationMapper = portOccupationMapper;
    }

    @Override
    public boolean isPortAvailable(Integer ipId, Integer port) {
        Long count = portOccupationMapper.selectCount(
                new LambdaQueryWrapper<EdgeIpPortOccupationEntity>()
                        .eq(EdgeIpPortOccupationEntity::getIpId, ipId)
                        .eq(EdgeIpPortOccupationEntity::getPort, port));
        return count == 0;
    }
}
