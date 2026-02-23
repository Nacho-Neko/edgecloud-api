package com.mikou.edgecloud.edge.domain.events;

import com.mikou.edgecloud.edge.domain.enums.IpPortOccupierType;

/**
 * 端口已占用确认事件
 */
public record PortOccupiedEvent(
        Integer ipId,
        Integer port,
        IpPortOccupierType occupierType,
        Integer occupierId
) {}
