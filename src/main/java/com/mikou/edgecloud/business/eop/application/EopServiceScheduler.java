package com.mikou.edgecloud.business.eop.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.business.domain.ProductStatus;
import com.mikou.edgecloud.business.eop.domain.events.EopServiceExpiredEvent;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.entity.EopServiceEntity;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.mapper.EopServiceMapper;
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
        
        // 1. 处理过期的服务 (ACTIVE -> EXPIRED)
        List<EopServiceEntity> expiredServices = eopServiceMapper.selectList(new LambdaQueryWrapper<EopServiceEntity>()
                .isNotNull(EopServiceEntity::getExpiredAt)
                .lt(EopServiceEntity::getExpiredAt, now)
                .eq(EopServiceEntity::getStatus, ProductStatus.ACTIVE)
                .isNull(EopServiceEntity::getRemovedAt));

        for (EopServiceEntity service : expiredServices) {
            expireService(service);
        }

        // 2. 处理欠费的服务 - 由外部计费系统调用 handleArrears() 方法来暂停服务
    }

    /**
     * 外部系统（如计费系统）发现欠费时，可以调用此方法来暂停服务
     */
    @Transactional
    public void handleArrears(UUID serviceTag) {
        EopServiceEntity service = eopServiceMapper.selectOne(new LambdaQueryWrapper<EopServiceEntity>()
                .eq(EopServiceEntity::getTag, serviceTag)
                .isNull(EopServiceEntity::getRemovedAt));
        if (service != null && service.getStatus() != ProductStatus.SUSPENDED) {
            suspendService(service);
        }
    }

    /**
     * 将服务标记为过期
     */
    private void expireService(EopServiceEntity service) {
        // 发布到期事件进行级联处理
        eventPublisher.publishEvent(new EopServiceExpiredEvent(service.getId(), service.getTag()));

        // 更新服务状态为过期
        service.setStatus(ProductStatus.EXPIRED);
        service.setUpdatedAt(Instant.now());
        eopServiceMapper.updateById(service);
    }

    /**
     * 将服务暂停（如欠费）
     */
    private void suspendService(EopServiceEntity service) {
        // 发布暂停事件进行级联暂停
        eventPublisher.publishEvent(new EopServiceExpiredEvent(service.getId(), service.getTag()));

        // 更新服务状态为暂停
        service.setStatus(ProductStatus.SUSPENDED);
        service.setUpdatedAt(Instant.now());
        eopServiceMapper.updateById(service);
    }
}