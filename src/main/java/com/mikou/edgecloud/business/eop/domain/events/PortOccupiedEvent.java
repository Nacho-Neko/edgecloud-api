package com.mikou.edgecloud.business.eop.domain.events;

import com.mikou.edgecloud.business.eop.domain.enums.EopOccupierType;

/**
 * 端口已占用事件（确认事件）
 */
public record PortOccupiedEvent(
        Integer ipId,
        Integer port,
        EopOccupierType occupierType,
        Integer occupierId
) {}
