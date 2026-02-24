package com.mikou.edgecloud.business.eop.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.business.domain.ProductStatus;
import com.mikou.edgecloud.business.domain.events.ServiceExpiredEvent;
import com.mikou.edgecloud.business.domain.events.ServiceSuspendedEvent;
import com.mikou.edgecloud.business.domain.product.ProductLifecycle;
import com.mikou.edgecloud.business.eop.application.EopService;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.entity.EopServiceEntity;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.mapper.EopServiceMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * EOP 产品生命周期管理实现（纵向切片）
 *
 * 职责：状态机流转 + 发布通用事件
 * 委托：实际业务逻辑委托给 EopService
 * 事件：状态变更后发布通用事件，EopServiceLifecycleListener 负责 EOP 特有的级联处理（暂停 Bound 等）
 */
@Component("EOP_ProductLifecycle")
public class EopProductLifecycle implements ProductLifecycle {

    private final EopService eopService;
    private final EopServiceMapper eopServiceMapper;
    private final ApplicationEventPublisher eventPublisher;

    public EopProductLifecycle(EopService eopService,
                               EopServiceMapper eopServiceMapper,
                               ApplicationEventPublisher eventPublisher) {
        this.eopService = eopService;
        this.eopServiceMapper = eopServiceMapper;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 购买 EOP 产品，创建服务实例。
     * params 必须包含：
     *   - productTag (String/UUID) : 产品 tag
     *   - durationMonths (Integer) : 购买时长（月）
     */
    @Override
    @Transactional
    public String createProduct(String accountId, Map<String, Object> params) {
        UUID ownerId  = UUID.fromString(accountId);
        UUID productTag = UUID.fromString(params.get("productTag").toString());
        Integer durationMonths = Integer.valueOf(params.get("durationMonths").toString());
        UUID serviceTag = eopService.purchaseProduct(ownerId, productTag, durationMonths);
        return serviceTag.toString();
    }

    @Override
    @Transactional
    public void activateProduct(String productId) {
        EopServiceEntity service = findByTag(productId);
        if (service.getStatus() == ProductStatus.ACTIVE) return;
        service.setStatus(ProductStatus.ACTIVE).setUpdatedAt(Instant.now());
        eopServiceMapper.updateById(service);
    }

    @Override
    @Transactional
    public void suspendProduct(String productId, String reason) {
        EopServiceEntity service = findByTag(productId);
        if (service.getStatus() == ProductStatus.SUSPENDED) return;
        service.setStatus(ProductStatus.SUSPENDED).setUpdatedAt(Instant.now());
        eopServiceMapper.updateById(service);
        // 级联暂停 Bound 由 EopServiceLifecycleListener 处理
        eventPublisher.publishEvent(new ServiceSuspendedEvent(service.getTag(), "EOP", reason));
    }

    @Override
    @Transactional
    public void resumeProduct(String productId) {
        EopServiceEntity service = findByTag(productId);
        if (service.getStatus() != ProductStatus.SUSPENDED)
            throw new IllegalStateException("Service is not suspended: " + productId);
        service.setStatus(ProductStatus.ACTIVE).setUpdatedAt(Instant.now());
        eopServiceMapper.updateById(service);
        // 恢复所有已暂停的 Bound
        eopService.resumeServiceBounds(service.getId());
    }

    @Override
    @Transactional
    public void expireProduct(String productId) {
        EopServiceEntity service = findByTag(productId);
        if (service.getStatus() == ProductStatus.EXPIRED) return;
        service.setStatus(ProductStatus.EXPIRED).setUpdatedAt(Instant.now());
        eopServiceMapper.updateById(service);
        eventPublisher.publishEvent(new ServiceExpiredEvent(service.getTag(), "EOP"));
    }

    @Override
    @Transactional
    public void deleteProduct(String productId) {
        EopServiceEntity service = findByTag(productId);
        service.setRemovedAt(Instant.now()).setUpdatedAt(Instant.now());
        eopServiceMapper.updateById(service);
        eventPublisher.publishEvent(new ServiceExpiredEvent(service.getTag(), "EOP"));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private EopServiceEntity findByTag(String productId) {
        EopServiceEntity service = eopServiceMapper.selectOne(
                new LambdaQueryWrapper<EopServiceEntity>()
                        .eq(EopServiceEntity::getTag, UUID.fromString(productId))
                        .isNull(EopServiceEntity::getRemovedAt));
        if (service == null) throw new IllegalArgumentException("EOP service not found: " + productId);
        return service;
    }
}