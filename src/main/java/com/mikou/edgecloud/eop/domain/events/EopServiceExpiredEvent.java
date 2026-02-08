package com.mikou.edgecloud.eop.domain.events;

import java.util.UUID;

/**
 * 领域事件：EOP 服务到期或删除
 */
public record EopServiceExpiredEvent(Integer serviceId, UUID serviceTag) {
}
