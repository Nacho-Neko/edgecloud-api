package com.mikou.edgecloud.edge.domain.events;

import com.mikou.edgecloud.edge.domain.enums.IpPortOccupierType;

/**
 * 端口释放请求事件
 */
public record PortReleaseRequestedEvent(
        Integer ipId,
        Integer port,
        IpPortOccupierType occupierType,
        Integer occupierId,
        boolean releaseAllByOccupier
) {
    /** 释放单个端口 */
    public static PortReleaseRequestedEvent single(Integer ipId, Integer port) {
        return new PortReleaseRequestedEvent(ipId, port, null, null, false);
    }

    /** 释放某个占用者的所有端口 */
    public static PortReleaseRequestedEvent allByOccupier(IpPortOccupierType occupierType, Integer occupierId) {
        return new PortReleaseRequestedEvent(null, null, occupierType, occupierId, true);
    }
}
