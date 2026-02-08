package com.mikou.edgecloud.eop.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.eop.api.dto.CreateInboundRequest;
import com.mikou.edgecloud.eop.api.dto.CreateOutboundRequest;
import com.mikou.edgecloud.eop.api.dto.EopBoundDto;
import com.mikou.edgecloud.eop.api.dto.EopServiceDto;
import com.mikou.edgecloud.eop.api.dto.PurchaseEopProductRequest;
import com.mikou.edgecloud.eop.application.EopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eop/services")
@Tag(name = "EopService")
public class EopServiceController {

    private final EopService eopService;

    public EopServiceController(EopService eopService) {
        this.eopService = eopService;
    }

    @PostMapping("/purchase")
    @Operation(summary = "购买 EOP 产品并创建服务")
    public ResponseEntity<UUID> purchase(@Valid @RequestBody PurchaseEopProductRequest request) {
        UUID tag = eopService.purchaseProduct(
                request.getOwnerId(),
                request.getProductTag(),
                request.getDurationMonths()
        );
        return ResponseEntity.ok(tag);
    }

    @PostMapping("/outbound")
    @Operation(summary = "创建 EOP 出站绑定")
    public ResponseEntity<UUID> createOutbound(@Valid @RequestBody CreateOutboundRequest request) {
        UUID tag = eopService.createOutbound(
                getCurrentAccountId(),
                request.getServiceTag(),
                request.getEopTag(),
                request.getAddrId(),
                request.getExtraParams()
        );
        return ResponseEntity.ok(tag);
    }

    @PostMapping("/inbound")
    @Operation(summary = "创建 EOP 进站绑定")
    public ResponseEntity<UUID> createInbound(@Valid @RequestBody CreateInboundRequest request) {
        UUID tag = eopService.createInbound(
                getCurrentAccountId(),
                request.getServiceTag(),
                request.getEopTag(),
                request.getAddrId(),
                request.getExtraParams()
        );
        return ResponseEntity.ok(tag);
    }

    private UUID getCurrentAccountId() {
        //  String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return UUID.fromString("18416cea-2781-477d-b420-763d40b17add");
    }

    @DeleteMapping("/inbound/{tag}")
    @Operation(summary = "销毁 EOP 进站绑定")
    public ResponseEntity<Void> deleteInbound(@PathVariable String tag) {
        eopService.deleteBoundByTag(tag);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/outbound/{tag}")
    @Operation(summary = "销毁 EOP 出站绑定")
    public ResponseEntity<Void> deleteOutbound(@PathVariable String tag) {
        eopService.deleteBoundByTag(tag);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{tag}/renew")
    @Operation(summary = "EOP 服务续费")
    public ResponseEntity<Void> renew(@PathVariable String tag, @RequestParam int months) {
        eopService.renewService(tag, months);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "获取当前用户的 EOP 产品（服务）列表")
    public ResponseEntity<Page<EopServiceDto>> listMyServices(@ParameterObject Pageable pageable) {
        UUID ownerId = getCurrentAccountId();
        return ResponseEntity.ok(eopService.listMyServices(ownerId, pageable));
    }

    @GetMapping("/bounds")
    @Operation(summary = "获取当前账户的所有 Bound，支持 EopTag 和 ServiceTag 筛选")
    public ResponseEntity<Page<EopBoundDto>> getBounds(@RequestParam(required = false) UUID eopTag,
                                                       @RequestParam(required = false) UUID serviceTag,
                                                       @ParameterObject Pageable pageable) {
        UUID ownerId = getCurrentAccountId();
        return ResponseEntity.ok(eopService.listBounds(ownerId, eopTag, serviceTag, pageable));
    }
}
