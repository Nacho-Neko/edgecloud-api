package com.mikou.edgecloud.edge.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.common.enums.FeatureType;
import com.mikou.edgecloud.edge.api.dto.*;
import com.mikou.edgecloud.edge.application.EdgeService;
import com.mikou.edgecloud.edge.domain.service.EdgeFeatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/edge")
@Tag(name = "Edge")
public class EdgeController {

    private final EdgeService edgeService;
    private final EdgeFeatureService featureService;

    public EdgeController(EdgeService edgeService, EdgeFeatureService featureService) {
        this.edgeService = edgeService;
        this.featureService = featureService;
    }

    @GetMapping("/edge/list")
    @Operation(summary = "分页查询边缘节点列表，支持区域筛选")
    public Page<EdgeItemDto> list(EdgeListQuery query,
                                  @ParameterObject
                                  Pageable pageable) {
        return edgeService.listEdges(query, pageable);
    }

    @PostMapping("/edge/create")
    @Operation(summary = "创建边缘节点")
    public Integer create(CreateEdgeRequest request) {
        return edgeService.create(request);
    }

    @GetMapping("/edge/{edgeTag}")
    @Operation(summary = "根据 edgeTag 查询节点详情")
    public EdgeDetailDto getByTag(@PathVariable("edgeTag") String edgeTag) {
        return edgeService.getEdgeDetail(edgeTag);
    }


    @PostMapping("/edge/capabilities/create")
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
}