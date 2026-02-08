package com.mikou.edgecloud.eop.domain.events;

import com.mikou.edgecloud.eop.domain.enums.EopOccupierType;

/**
 * 端口释放请求事件
 */
public record PortReleaseRequestedEvent(
        Integer ipId,
        Integer port,
        EopOccupierType occupierType,
        Integer occupierId,
        boolean releaseAllByOccupier
) {
    public static PortReleaseRequestedEvent single(Integer ipId, Integer port) {
        return new PortReleaseRequestedEvent(ipId, port, null, null, false);
    }

    public static PortReleaseRequestedEvent allByOccupier(EopOccupierType occupierType, Integer occupierId) {
        return new PortReleaseRequestedEvent(null, null, occupierType, occupierId, true);
    }
}
