package com.mikou.edgecloud.edge.domain.events;

import com.mikou.edgecloud.edge.domain.enums.IpPortOccupierType;

/**
 * 端口申请请求事件
 * 由任意业务层发布，由 Edge 层的端口管理服务处理。
 */
public record PortApplyRequestedEvent(
        Integer ipId,
        Integer port,
        IpPortOccupierType occupierType,
        Integer occupierId
) {}
