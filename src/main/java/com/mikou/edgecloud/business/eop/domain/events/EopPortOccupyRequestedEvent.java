package com.mikou.edgecloud.business.eop.domain.events;

/**
 * 端口占用请求事件（由 EOP 业务层发布，Edge 层监听并执行）
 *
 * @param ipId         edge_nic_ip.id
 * @param port         需要占用的端口
 * @param occupierType 占用者类型字符串，如 "EOP_BOUND"
 * @param occupierId   占用者 ID（entity 插入后的主键）
 */
public record EopPortOccupyRequestedEvent(
        Integer ipId,
        Integer port,
        String occupierType,
        Integer occupierId
) {}
