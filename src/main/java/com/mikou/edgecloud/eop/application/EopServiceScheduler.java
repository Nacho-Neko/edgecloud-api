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

@Component
public class EopServiceScheduler {

    private final EopServiceMapper eopServiceMapper;
    private final ApplicationEventPublisher eventPublisher;

    public EopServiceScheduler(EopServiceMapper eopServiceMapper, ApplicationEventPublisher eventPublisher) {
        this.eopServiceMapper = eopServiceMapper;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 每小时扫描一次过期的服务
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void scanExpiredServices() {
        Instant now = Instant.now();
        List<EopServiceEntity> expiredServices = eopServiceMapper.selectList(new LambdaQueryWrapper<EopServiceEntity>()
                .isNotNull(EopServiceEntity::getExpiredAt)
                .lt(EopServiceEntity::getExpiredAt, now)
                .isNull(EopServiceEntity::getRemovedAt));

        for (EopServiceEntity service : expiredServices) {
            // 发布到期事件进行级联回收
            eventPublisher.publishEvent(new EopServiceExpiredEvent(service.getId(), service.getTag()));

            // 标记服务已移除或更新状态
            service.setStatus(EopServiceStatus.SUSPENDED);
            service.setRemovedAt(now);
            eopServiceMapper.updateById(service);
        }
    }
}
