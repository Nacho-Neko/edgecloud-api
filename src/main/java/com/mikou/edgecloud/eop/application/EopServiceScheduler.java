package com.mikou.edgecloud.eop.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.eop.domain.enums.EopServiceStatus;
import com.mikou.edgecloud.eop.domain.events.EopServiceExpiredEvent;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.EopServiceEntity;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopServiceMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class EopServiceScheduler {

    private final EopServiceMapper eopServiceMapper;
    private final ApplicationEventPublisher eventPublisher;

    public EopServiceScheduler(EopServiceMapper eopServiceMapper, ApplicationEventPublisher eventPublisher) {
        this.eopServiceMapper = eopServiceMapper;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 每小时扫描一次过期或欠费的服务
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void scanServices() {
        Instant now = Instant.now();
        
        // 1. 处理过期的服务 (ACTIVE -> SUSPENDED)
        List<EopServiceEntity> expiredServices = eopServiceMapper.selectList(new LambdaQueryWrapper<EopServiceEntity>()
                .isNotNull(EopServiceEntity::getExpiredAt)
                .lt(EopServiceEntity::getExpiredAt, now)
                .eq(EopServiceEntity::getStatus, EopServiceStatus.ACTIVE)
                .isNull(EopServiceEntity::getRemovedAt));

        for (EopServiceEntity service : expiredServices) {
            suspendService(service);
        }

        // 2. 处理欠费的服务
        // 假设计费系统通过某种方式标记了 SUSPENDED，但为了确保所有相关的 Bound 都已暂停，
        // 我们可以在这里定期扫描 SUSPENDED 状态的服务并再次触发事件，
        // 或者提供一个供计费系统调用的 RPC/API。
        // 这里我们实现一个通用的根据状态触发暂停的逻辑，以应对可能的异步或遗漏。
    }

    /**
     * 外部系统（如计费系统）发现欠费时，可以调用此方法来暂停服务
     */
    @Transactional
    public void handleArrears(UUID serviceTag) {
        EopServiceEntity service = eopServiceMapper.selectOne(new LambdaQueryWrapper<EopServiceEntity>()
                .eq(EopServiceEntity::getTag, serviceTag)
                .isNull(EopServiceEntity::getRemovedAt));
        if (service != null && service.getStatus() != EopServiceStatus.SUSPENDED) {
            suspendService(service);
        }
    }

    private void suspendService(EopServiceEntity service) {
        // 发布到期事件进行级联暂停
        eventPublisher.publishEvent(new EopServiceExpiredEvent(service.getId(), service.getTag()));

        // 更新服务状态为暂停
        service.setStatus(EopServiceStatus.SUSPENDED);
        service.setUpdatedAt(Instant.now());
        eopServiceMapper.updateById(service);
    }
}
