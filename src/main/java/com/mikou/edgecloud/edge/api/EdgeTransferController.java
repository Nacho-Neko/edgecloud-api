package com.mikou.edgecloud.edge.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.edge.api.dto.*;
import com.mikou.edgecloud.edge.application.EdgeProtocolService;
import com.mikou.edgecloud.edge.application.EdgeTransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/edge/transfer")
@Tag(name = "EdgeTransfer", description = "Edge 节点间传输路径与监听器管理")
public class EdgeTransferController {

    private final EdgeTransferService edgeTransferService;
    private final EdgeProtocolService edgeProtocolService;

    public EdgeTransferController(EdgeTransferService edgeTransferService,
                                  EdgeProtocolService edgeProtocolService) {
        this.edgeTransferService = edgeTransferService;
        this.edgeProtocolService = edgeProtocolService;
    }

    // ── 传输路径 ──────────────────────────────────────────────────────────────

    @PostMapping("/create")
    @Operation(summary = "创建节点间传输路径",
               description = "senderEdge 作为客户端连接到 targetEdge 的某个监听器（EdgeProtocol）")
    public ResponseEntity<EdgeTransferDto> createTransfer(@RequestBody CreateEdgeTransferRequest request) {
        return ResponseEntity.ok(edgeTransferService.createTransfer(
                request.getSenderEdgeTag(),
                request.getSendIpId(),
                request.getTargetProtocolId(),
                request.getHostName(),
                request.getRemark()
        ));
    }

    @DeleteMapping("/{pairKey}")
    @Operation(summary = "删除节点间传输路径（通过 pairKey）")
    public ResponseEntity<Void> deleteTransfer(@PathVariable("pairKey") UUID pairKey) {
        edgeTransferService.deleteTransfer(pairKey);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询节点的传输路径列表",
               description = "返回指定 Edge Tag 参与的所有传输路径（含发送端和接收端）")
    public Page<EdgeTransferDto> listTransfers(
            @RequestParam("edgeTag") UUID edgeTag,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return edgeTransferService.listTransfersByEdgeTag(edgeTag, pageable);
    }

    // ── 监听器（EdgeProtocol）管理 ─────────────────────────────────────────────

    @PostMapping("/protocols")
    @Operation(summary = "为 Edge 节点创建传输监听器",
               description = "在指定节点的 IP:Port 上注册协议监听，供其他节点的传输路径连入")
    public ResponseEntity<EdgeProtocolDto> createProtocol(@RequestBody CreateEdgeProtocolRequest request) {
        return ResponseEntity.ok(edgeProtocolService.createProtocol(
                request.getEdgeTag(),
                request.getProtocol(),
                request.getIpId(),
                request.getPort(),
                request.getPortRangeStart(),
                request.getPortRangeEnd()
        ));
    }

    @DeleteMapping("/protocols/{id}")
    @Operation(summary = "删除 Edge 节点传输监听器")
    public ResponseEntity<Void> destroyProtocol(@PathVariable("id") Integer id) {
        edgeProtocolService.destroyProtocol(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/protocols")
    @Operation(summary = "分页查询 Edge 节点的传输监听器列表")
    public Page<EdgeProtocolDto> listProtocols(
            @RequestParam("edgeTag") UUID edgeTag,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return edgeProtocolService.listProtocolsByEdgeTag(edgeTag, pageable);
    }
}