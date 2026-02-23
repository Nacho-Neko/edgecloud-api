package com.mikou.edgecloud.business.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.account.domain.service.AccountDomainService;
import com.mikou.edgecloud.business.api.dto.BusinessServiceDto;
import com.mikou.edgecloud.business.domain.ProductStatus;
import com.mikou.edgecloud.business.eop.application.EopService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Business 管理应用服务
 * 负责业务服务的查询和管理
 * 
 * 架构说明：
 * 1. 通过 AccountDomainService 调用 account 领域能力
 * 2. 聚合不同业务类型的服务查询（EOP、Edge、IoT等）
 * 3. 为微服务拆分做准备
 */
@Service
public class BusinessAdminService {

    private final AccountDomainService accountDomainService;
    private final EopService eopService;

    public BusinessAdminService(AccountDomainService accountDomainService,
                                EopService eopService) {
        this.accountDomainService = accountDomainService;
        this.eopService = eopService;
    }

    /**
     * 查询 Business 服务列表 - 支持筛选条件
     * 通过各业务应用服务层完成调用
     *
     * @param accountId 账户ID（可选）
     * @param businessType 业务类型（可选），如 "EOP"
     * @param status 服务状态（可选）
     * @param page 页码
     * @param size 每页大小
     * @return 分页的服务列表
     */
    public Page<BusinessServiceDto> listBusinessServices(UUID accountId, String businessType,
                                                         ProductStatus status, int page, int size) {
        // 如果指定了 accountId，验证账户是否存在（通过领域服务）
        if (accountId != null) {
            accountDomainService.validateAccountExists(accountId);
        }

        // 根据业务类型动态调用对应的服务层
        // 目前只实现了 EOP，其他业务类型可以后续扩展
        if (businessType == null || "EOP".equalsIgnoreCase(businessType)) {
            // 通过 EOP 应用服务层查询服务列表
            Pageable pageable = PageRequest.of(page, size);
            Page<?> result = eopService.listServicesWithFilter(accountId, status, pageable);

            // 转换为 BusinessServiceDto 类型
            Page<BusinessServiceDto> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
            dtoPage.setRecords((List<BusinessServiceDto>) (List<?>) result.getRecords());
            return dtoPage;
        } else {
            // 其他业务类型暂未实现，返回空结果
            Page<BusinessServiceDto> emptyPage = new Page<>(page + 1, size, 0);
            emptyPage.setRecords(List.of());
            return emptyPage;
        }
    }
}
