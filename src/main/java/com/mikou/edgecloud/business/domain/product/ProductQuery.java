package com.mikou.edgecloud.business.domain.product;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.business.api.dto.BusinessServiceDto;
import com.mikou.edgecloud.business.domain.ProductStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * 产品查询 SPI
 * 各业务域实现此接口，business 顶层通过 ProductCapabilityLocator 调用，不直接依赖任何子域。
 */
public interface ProductQuery {
    /**
     * 分页查询该业务域的服务列表（供管理后台使用）
     *
     * @param ownerId  账户 ID（可选）
     * @param status   服务状态（可选）
     * @param pageable 分页参数
     */
    Page<? extends BusinessServiceDto> listServices(UUID ownerId, ProductStatus status, Pageable pageable);

    /**
     * 查询单条服务详情
     *
     * @param serviceTag 服务唯一标识
     */
    BusinessServiceDto getService(UUID serviceTag);

    /**
     * 查询服务状态
     */
    ProductStatus getServiceStatus(UUID serviceTag);
}