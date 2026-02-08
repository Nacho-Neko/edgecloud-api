package com.mikou.edgecloud.eop.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.common.enums.FeatureType;
import com.mikou.edgecloud.common.events.EdgeFeatureCreatedEvent;
import com.mikou.edgecloud.common.events.EdgeFeatureDisabledEvent;
import com.mikou.edgecloud.common.events.EdgeFeatureEnabledEvent;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.EopAppEntity;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopAppMapper;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EopAppAutoCreateListener {

    private final EopAppMapper eopAppMapper;

    public EopAppAutoCreateListener(EopAppMapper eopAppMapper) {
        this.eopAppMapper = eopAppMapper;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreated(EdgeFeatureCreatedEvent event) {
        if (event.featureType() != FeatureType.EOP || event.edgeId() == null) {
            return;
        }

        // 幂等：同一个 edge 只允许一个 eop_app
        Long existing = eopAppMapper.selectCount(new LambdaQueryWrapper<EopAppEntity>()
                .eq(EopAppEntity::getEdgeId, event.edgeId()));
        if (existing != null && existing > 0) {
            return;
        }

        Instant now = Instant.now();
        EopAppEntity app = new EopAppEntity()
                .setEopTag(UUID.randomUUID())
                .setEdgeId(event.edgeId())
                .setCreatedAt(now)
                .setUpdatedAt(now);

        eopAppMapper.insert(app);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onEnabled(EdgeFeatureEnabledEvent event) {
        // 目前不处理启用事件
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDisabled(EdgeFeatureDisabledEvent event) {
        // 目前不处理禁用事件
    }
}