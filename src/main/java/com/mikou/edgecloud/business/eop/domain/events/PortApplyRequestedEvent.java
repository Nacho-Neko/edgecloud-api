package com.mikou.edgecloud.business.eop.domain.events;

import com.mikou.edgecloud.business.eop.domain.enums.EopOccupierType;

/**
 * 端口申请请求事件
 */
public record PortApplyRequestedEvent(
        Integer ipId,
        Integer port,
        EopOccupierType occupierType,
        Integer occupierId
) {}
