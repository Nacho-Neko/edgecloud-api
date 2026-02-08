package com.mikou.edgecloud.edge.api;

import com.mikou.edgecloud.edge.api.dto.CreateRegionRequest;
import com.mikou.edgecloud.edge.api.dto.RegionTreeDto;
import com.mikou.edgecloud.edge.application.EdgeRegionService;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeAreaEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/edge/regions")
@Tag(name = "EdgeRegion")
public class EdgeRegionController {

    private final EdgeRegionService regionService;

    public EdgeRegionController(EdgeRegionService regionService) {
        this.regionService = regionService;
    }

    @PostMapping
    @Operation(summary = "创建地域")
    public ResponseEntity<Integer> create(CreateRegionRequest req) {
        return ResponseEntity.ok(regionService.create(req));
    }

    @GetMapping
    @Operation(summary = "获取地域树")
    public List<RegionTreeDto> getTree() {
        return regionService.getTree();
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "通过 ID 删除地域")
    public void delete(@PathVariable("id") Integer id) {
        regionService.delete(id);
    }
}