package com.mikou.edgecloud.business.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.business.api.dto.BusinessServiceDto;
import com.mikou.edgecloud.business.api.dto.BusinessCapabilityDto;
import com.mikou.edgecloud.business.application.BusinessAdminService;
import com.mikou.edgecloud.business.application.BusinessCapabilityRegistry;
import com.mikou.edgecloud.business.domain.ProductStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/admin/business")
@Tag(name = "业务管理", description = "业务服务管理相关接口")
public class BusinessAdminController {

    private final BusinessAdminService businessAdminService;
    private final BusinessCapabilityRegistry capabilityRegistry;

    public BusinessAdminController(BusinessAdminService businessAdminService,
                                   BusinessCapabilityRegistry capabilityRegistry) {
        this.businessAdminService = businessAdminService;
        this.capabilityRegistry = capabilityRegistry;
    }

    @GetMapping("/capabilities")
    @Operation(summary = "查询系统已注册的业务能力", 
               description = "返回系统中所有已注册的业务能力，如 EOP 等")
    public List<BusinessCapabilityDto> getCapabilities() {
        return capabilityRegistry.getCapabilities();
    }

    @GetMapping("/capabilities/enabled")
    @Operation(summary = "查询已启用的业务能力")
    public List<BusinessCapabilityDto> getEnabledCapabilities() {
        return capabilityRegistry.getEnabledCapabilities();
    }

    @GetMapping("/list")
    @Operation(summary = "查询业务列表", description = "查询 Business 服务列表，支持按账户ID、业务类型和状态筛选")
    public Page<BusinessServiceDto> listBusiness(
            @Parameter(description = "账户ID（可选）")
            @RequestParam(required = false) UUID accountId,
            @Parameter(description = "业务类型（可选），如：EOP")
            @RequestParam(required = false) String businessType,
            @Parameter(description = "服务状态（可选）：ACTIVE, SUSPENDED, EXPIRED")
            @RequestParam(required = false) ProductStatus status,
            @Parameter(description = "页码，从0开始")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小")
            @RequestParam(defaultValue = "10") int size) {
        return businessAdminService.listBusinessServices(accountId, businessType, status, page, size);
    }
}