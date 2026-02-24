package com.mikou.edgecloud.business.eop.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.business.api.dto.BusinessServiceDto;
import com.mikou.edgecloud.business.domain.ProductStatus;
import com.mikou.edgecloud.business.domain.product.ProductQuery;
import com.mikou.edgecloud.business.eop.api.dto.EopServiceDto;
import com.mikou.edgecloud.business.eop.application.EopService;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.entity.EopServiceEntity;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.mapper.EopServiceMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * EOP 产品查询实现（纵向切片：business 顶层通过 ProductCapabilityLocator 调用，不直接依赖 EopService）
 */
@Component("EOP_ProductQuery")
public class EopProductQuery implements ProductQuery {

    private final EopService eopService;
    private final EopServiceMapper eopServiceMapper;

    public EopProductQuery(EopService eopService, EopServiceMapper eopServiceMapper) {
        this.eopService = eopService;
        this.eopServiceMapper = eopServiceMapper;
    }

    @Override
    public Page<EopServiceDto> listServices(UUID ownerId, ProductStatus status, Pageable pageable) {
        return eopService.listServicesWithFilter(ownerId, status, pageable);
    }

    @Override
    public BusinessServiceDto getService(UUID serviceTag) {
        return eopService.getServiceByTagDto(serviceTag);
    }

    @Override
    public ProductStatus getServiceStatus(UUID serviceTag) {
        EopServiceEntity entity = eopServiceMapper.selectOne(
                new LambdaQueryWrapper<EopServiceEntity>()
                        .eq(EopServiceEntity::getTag, serviceTag)
                        .isNull(EopServiceEntity::getRemovedAt));
        if (entity == null) throw new IllegalArgumentException("EOP service not found: " + serviceTag);
        return entity.getStatus();
    }
}