package com.mikou.edgecloud.business.eop.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.business.eop.api.dto.EopAppDto;
import com.mikou.edgecloud.business.eop.api.dto.EopBoundDto;
import com.mikou.edgecloud.edge.api.dto.EdgeListQuery;
import com.mikou.edgecloud.business.eop.application.EopAppService;
import com.mikou.edgecloud.business.eop.domain.model.EopSettings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/business/eop")
@Tag(name = "EopApps")
public class EopAdminController {

    private final EopAppService eopAppService;

    public EopAdminController(EopAppService eopAppService) {
        this.eopAppService = eopAppService;
    }

    @GetMapping("/list")
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

    @GetMapping("/bounds")
    public Page<EopBoundDto> listBounds(@RequestParam UUID eopTag,
                                        @ParameterObject Pageable pageable) {
        return eopAppService.listBoundsByEopTag(eopTag, pageable);
    }
}