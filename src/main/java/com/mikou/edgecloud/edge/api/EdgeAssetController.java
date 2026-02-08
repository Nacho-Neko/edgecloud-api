package com.mikou.edgecloud.edge.api;

import com.mikou.edgecloud.common.enums.FeatureType;
import com.mikou.edgecloud.edge.api.dto.EdgeNicDto;
import com.mikou.edgecloud.edge.application.EdgeAssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/edge/assets")
@Tag(name = "EdgeAsset")
public class EdgeAssetController {

    private final EdgeAssetService assetService;

    public EdgeAssetController(EdgeAssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping("/nic/{edgeTag}")
    @Operation(summary = "获取 Edge 的网卡和 IP 列表")
    public ResponseEntity<List<EdgeNicDto>> getEdgeNics(@PathVariable String edgeTag) {
        List<EdgeNicDto> nics = assetService.getEdgeNics(edgeTag);
        return ResponseEntity.ok(nics);
    }

    @PutMapping("/nic/ip")
    @Operation(summary = "Set public IP")
    public void setPublicIp(
            Integer ipId,
            String publicIp
    ) {
        assetService.setPublicIp(ipId, publicIp);
    }

    @PostMapping("/nic/ip/allocate")
    @Operation(summary = "将 IP 分配给具体模块资源")
    public ResponseEntity<String> allocate(
            @RequestParam Integer ipId,
            @RequestParam FeatureType capabilityType,
            @RequestParam UUID resourceTag
    ) {
        boolean success = assetService.allocateIp(ipId, capabilityType, resourceTag);
        return success ? ResponseEntity.ok("IP allocated successfully")
                : ResponseEntity.badRequest().body("Failed to allocate IP");
    }
}