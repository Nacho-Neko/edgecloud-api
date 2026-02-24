package com.mikou.edgecloud.business.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.account.domain.service.AccountDomainService;
import com.mikou.edgecloud.business.api.dto.BusinessServiceDto;
import com.mikou.edgecloud.business.domain.ProductStatus;
import com.mikou.edgecloud.business.domain.product.ProductLifecycle;
import com.mikou.edgecloud.business.domain.product.ProductQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Business 管理应用服务
 * 通过 ProductCapabilityLocator 路由到对应业务域，不直接依赖任何子域（EOP、CDN 等）。
 */
@Service
public class BusinessAdminService {

    private final AccountDomainService accountDomainService;
    private final ProductCapabilityLocator capabilityLocator;

    public BusinessAdminService(AccountDomainService accountDomainService,
                                ProductCapabilityLocator capabilityLocator) {
        this.accountDomainService = accountDomainService;
        this.capabilityLocator = capabilityLocator;
    }

    /**
     * 分页查询业务服务列表
     *
     * @param businessType 业务类型（必填），如 "EOP"
     */
    public Page<? extends BusinessServiceDto> listBusinessServices(UUID accountId,
                                                                   String businessType,
                                                                   ProductStatus status,
                                                                   int page, int size) {
        if (accountId != null) {
            accountDomainService.validateAccountExists(accountId);
        }

        if (businessType == null || businessType.isBlank()) {
            throw new IllegalArgumentException("businessType is required");
        }

        ProductQuery query = capabilityLocator.getQuery(businessType.toUpperCase());
        if (query == null) {
            throw new IllegalArgumentException("Unsupported businessType: " + businessType);
        }

        Pageable pageable = PageRequest.of(page, size);
        return query.listServices(accountId, status, pageable);
    }

    /**
     * 查询单条服务详情
     */
    public BusinessServiceDto getService(String businessType, UUID serviceTag) {
        ProductQuery query = capabilityLocator.getQuery(businessType.toUpperCase());
        if (query == null) throw new IllegalArgumentException("Unsupported businessType: " + businessType);
        return query.getService(serviceTag);
    }

    /**
     * 暂停服务（欠费、违规等）
     */
    public void suspendService(String businessType, UUID serviceTag, String reason) {
        ProductLifecycle lifecycle = capabilityLocator.getLifecycle(businessType.toUpperCase());
        if (lifecycle == null) throw new IllegalArgumentException("Unsupported businessType: " + businessType);
        lifecycle.suspendProduct(serviceTag.toString(), reason);
    }

    /**
     * 恢复服务
     */
    public void resumeService(String businessType, UUID serviceTag) {
        ProductLifecycle lifecycle = capabilityLocator.getLifecycle(businessType.toUpperCase());
        if (lifecycle == null) throw new IllegalArgumentException("Unsupported businessType: " + businessType);
        lifecycle.resumeProduct(serviceTag.toString());
    }

    /**
     * 手动过期服务
     */
    public void expireService(String businessType, UUID serviceTag) {
        ProductLifecycle lifecycle = capabilityLocator.getLifecycle(businessType.toUpperCase());
        if (lifecycle == null) throw new IllegalArgumentException("Unsupported businessType: " + businessType);
        lifecycle.expireProduct(serviceTag.toString());
    }
}