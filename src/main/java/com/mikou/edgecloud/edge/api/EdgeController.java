package com.mikou.edgecloud.edge.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.common.enums.FeatureType;
import com.mikou.edgecloud.edge.api.dto.*;
import com.mikou.edgecloud.edge.application.EdgeService;
import com.mikou.edgecloud.edge.domain.service.EdgeFeatureService;
import com.mikou.edgecloud.business.application.BusinessCapabilityRegistry;
import com.mikou.edgecloud.business.api.dto.BusinessCapabilityDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/admin/edge")
@Tag(name = "Edge")
public class EdgeController {

    private final EdgeService edgeService;
    private final EdgeFeatureService featureService;
    private final BusinessCapabilityRegistry capabilityRegistry;

    public EdgeController(EdgeService edgeService,
                          EdgeFeatureService featureService,
                          BusinessCapabilityRegistry capabilityRegistry) {
        this.edgeService = edgeService;
        this.featureService = featureService;
        this.capabilityRegistry = capabilityRegistry;
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询边缘节点列表，支持区域筛选")
    public Page<EdgeItemDto> list(EdgeListQuery query,
                                  @ParameterObject
                                  Pageable pageable) {
        return edgeService.listEdges(query, pageable);
    }

    @GetMapping("/{edgeId}")
    @Operation(summary = "根据 edgeId 查询节点详情")
    public EdgeDetailDto getById(@PathVariable("edgeId") Integer edgeId) {
        return edgeService.getEdgeDetail(edgeId);
    }

    @GetMapping("/{edgeId}/system-info")
    @Operation(summary = "获取 Edge 节点的系统信息（硬件配置、操作系统、版本等）")
    public EdgeSystemInfoDto getSystemInfo(@PathVariable("edgeId") Integer edgeId) {
        return edgeService.getEdgeSystemInfo(edgeId);
    }

    @PostMapping("/{edgeId}/metrics")
    @Operation(summary = "获取 Edge 节点的监控指标时序数据（CPU、内存使用率）",
               description = "用于生成监控图表。支持时间范围查询和数据采样。如果不传时间参数，默认返回最近1小时的数据。")
    public EdgeMetricsResponse getMetrics(
            @PathVariable("edgeId") Integer edgeId,
            @RequestBody(required = false) EdgeMetricsRequest request
    ) {
        Instant start;
        Instant end;
        Integer limit = null;
        
        // 处理请求参数
        if (request == null || (request.getStartTime() == null && request.getEndTime() == null)) {
            // 默认返回最近1小时的数据
            end = Instant.now();
            start = end.minusSeconds(3600);
        } else {
            // 将时间戳（毫秒）转换为 Instant
            start = request.getStartTime() != null 
                ? Instant.ofEpochMilli(request.getStartTime()) 
                : Instant.now().minusSeconds(3600);
            end = request.getEndTime() != null 
                ? Instant.ofEpochMilli(request.getEndTime()) 
                : Instant.now();
        }
        
        if (request != null) {
            limit = request.getLimit();
        }
        
        return edgeService.getEdgeMetrics(edgeId, start, end, limit);
    }

    @PostMapping("/create")
    @Operation(summary = "创建边缘节点")
    public Integer create(CreateEdgeRequest request) {
        return edgeService.create(request);
    }

    @PostMapping("/capabilities/create")
    @Operation(summary = "创建能力")
    public ResponseEntity<Void> createCapability(
            String edgeTag,
            FeatureType capability
    ) {
        featureService.createFeature(edgeTag, capability);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edge/capabilities/enable")
    @Operation(summary = "启用能力")
    public void enableCapability(
            String edgeTag,
            FeatureType capability
    ) {
        featureService.enableFeature(edgeTag, capability);
    }

    @PutMapping("/edge/capabilities/disable")
    @Operation(summary = "禁用能力")
    public void disableCapability(
            String edgeTag,
            FeatureType capability
    ) {
        featureService.destroyFeature(edgeTag, capability);
    }

    @DeleteMapping("/edge/capabilities/destroy")
    @Operation(summary = "销毁能力")
    public ResponseEntity<Void> destroyCapability(
            String edgeTag,
            FeatureType capability
    ) {
        featureService.destroyFeature(edgeTag, capability);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{edgeId}/info")
    @Operation(summary = "更新 Edge 节点的硬件和系统信息")
    public ResponseEntity<Void> updateEdgeInfo(
            @PathVariable("edgeId") Integer edgeId,
            @RequestBody UpdateEdgeInfoRequest request
    ) {
        edgeService.updateEdgeInfo(edgeId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/capabilities")
    @Operation(summary = "查询系统已注册的业务能力", 
               description = "返回系统中所有已注册的业务能力，如 EOP 等")
    public ResponseEntity<java.util.List<BusinessCapabilityDto>> getCapabilities() {
        return ResponseEntity.ok(capabilityRegistry.getCapabilities());
    }

    @GetMapping("/capabilities/enabled")
    @Operation(summary = "查询已启用的业务能力")
    public ResponseEntity<java.util.List<BusinessCapabilityDto>> getEnabledCapabilities() {
        return ResponseEntity.ok(capabilityRegistry.getEnabledCapabilities());
    }
}