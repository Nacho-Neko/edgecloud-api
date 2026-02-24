package com.mikou.edgecloud.business.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.business.api.dto.BusinessCapabilityDto;
import com.mikou.edgecloud.business.api.dto.BusinessServiceDto;
import com.mikou.edgecloud.business.application.BusinessAdminService;
import com.mikou.edgecloud.business.application.BusinessCapabilityRegistry;
import com.mikou.edgecloud.business.domain.ProductStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "查询系统已注册的业务能力")
    public List<BusinessCapabilityDto> getCapabilities() {
        return capabilityRegistry.getCapabilities();
    }

    @GetMapping("/capabilities/enabled")
    @Operation(summary = "查询已启用的业务能力")
    public List<BusinessCapabilityDto> getEnabledCapabilities() {
        return capabilityRegistry.getEnabledCapabilities();
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询业务服务列表",
               description = "businessType 必填，如 EOP；支持按 accountId、status 过滤")
    public Page<? extends BusinessServiceDto> listBusiness(
            @RequestParam String businessType,
            @RequestParam(required = false) UUID accountId,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return businessAdminService.listBusinessServices(accountId, businessType, status, page, size);
    }

    @GetMapping("/service")
    @Operation(summary = "查询单条服务详情")
    public ResponseEntity<BusinessServiceDto> getService(
            @RequestParam String businessType,
            @RequestParam UUID serviceTag) {
        return ResponseEntity.ok(businessAdminService.getService(businessType, serviceTag));
    }

    @PostMapping("/service/suspend")
    @Operation(summary = "暂停服务（欠费、违规等）")
    public ResponseEntity<Void> suspendService(
            @RequestParam String businessType,
            @RequestParam UUID serviceTag,
            @RequestParam(defaultValue = "manual") String reason) {
        businessAdminService.suspendService(businessType, serviceTag, reason);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/service/resume")
    @Operation(summary = "恢复服务")
    public ResponseEntity<Void> resumeService(
            @RequestParam String businessType,
            @RequestParam UUID serviceTag) {
        businessAdminService.resumeService(businessType, serviceTag);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/service/expire")
    @Operation(summary = "手动过期服务")
    public ResponseEntity<Void> expireService(
            @RequestParam String businessType,
            @RequestParam UUID serviceTag) {
        businessAdminService.expireService(businessType, serviceTag);
        return ResponseEntity.ok().build();
    }
}