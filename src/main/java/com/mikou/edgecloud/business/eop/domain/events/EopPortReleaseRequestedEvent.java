package com.mikou.edgecloud.business.eop.domain.events;

/**
 * 端口释放请求事件（释放某占用者的所有端口）
 *
 * @param occupierType 占用者类型字符串，如 "EOP_BOUND"
 * @param occupierId   占用者 ID
 */
public record EopPortReleaseRequestedEvent(
        String occupierType,
        Integer occupierId
) {}
