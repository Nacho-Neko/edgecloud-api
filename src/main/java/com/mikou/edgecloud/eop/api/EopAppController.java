package com.mikou.edgecloud.eop.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.edge.api.dto.EdgeListQuery;
import com.mikou.edgecloud.eop.api.dto.*;
import com.mikou.edgecloud.eop.application.EopAppService;
import com.mikou.edgecloud.eop.application.EopTransferService;
import com.mikou.edgecloud.eop.domain.model.EopSettings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/eop/apps")
@Tag(name = "EopApps")
public class EopAppController {

    private final EopAppService eopAppService;
    private final EopTransferService eopTransferService;

    public EopAppController(EopAppService eopAppService, EopTransferService eopTransferService) {
        this.eopAppService = eopAppService;
        this.eopTransferService = eopTransferService;
    }

    @GetMapping
    @Operation(summary = "分页查询 EOP 应用列表，支持地域筛选")
    public Page<EopAppDto> listApps(EdgeListQuery query,
                                    @ParameterObject Pageable pageable) {
        return eopAppService.listApps(query, pageable);
    }

    @PostMapping("/sync")
    @Operation(summary = "手动同步 EOP 应用列表")
    public void syncApps() {
        eopAppService.syncApps();
    }

    @PutMapping("/settings")
    public EopAppDto updateSettings(@RequestParam UUID eopTag, @RequestBody EopSettings settings) {
        return eopAppService.updateSettings(eopTag, settings);
    }

    @PostMapping("/protocols")
    @Operation(summary = "为 EOP 应用添加协议监听器")
    public void createProtocol(@RequestParam UUID eopTag,
                               @RequestParam String protocol,
                               @RequestBody EopSettings.ProtocolListener listener) {
        eopAppService.createProtocol(eopTag, protocol, listener);
    }

    @DeleteMapping("/protocols")
    @Operation(summary = "移除 EOP 应用的协议监听器")
    public void destroyProtocol(@RequestParam UUID eopTag,
                                @RequestParam String protocol,
                                @RequestParam Integer port) {
        eopAppService.destroyProtocol(eopTag, protocol, port);
    }

    @GetMapping("/bounds")
    public Page<EopBoundDto> listBounds(@RequestParam UUID eopTag,
                                        @ParameterObject Pageable pageable) {
        return eopAppService.listBoundsByEopTag(eopTag, pageable);
    }

    @GetMapping("/transfers")
    public List<EopTransferDto> listTransfers(@RequestParam String eopTag) {
        return eopTransferService.listTransfersByEopTag(eopTag);
    }

    @PostMapping("/transfers")
    public EopTransferDto createTransfer(@Valid @RequestBody CreateEopTransferRequest request) {
        return eopTransferService.createTransfer(
                request.getSenderEopTag(),
                request.getTargetEopTag(),
                request.getSendIpId(),
                request.getReceiveIpId(),
                request.getProtocol(),
                request.getHostName(),
                request.getRemark()
        );
    }

    @DeleteMapping("/transfers/{id}")
    public void deleteTransfer(@PathVariable Integer id) {
        eopTransferService.deleteTransfer(id);
    }
}
