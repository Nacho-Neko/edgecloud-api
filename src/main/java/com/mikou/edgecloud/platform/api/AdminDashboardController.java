package com.mikou.edgecloud.platform.api;

import com.mikou.edgecloud.platform.api.dto.DashboardSummaryDto;
import com.mikou.edgecloud.platform.application.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员 Dashboard API
 */
@RestController
@RequestMapping("/api/admin/dashboard")
@Tag(name = "Admin Dashboard", description = "管理员仪表盘接口")
public class AdminDashboardController {
    
    private final DashboardService dashboardService;
    
    public AdminDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    
    @GetMapping("/summary")
    @Operation(summary = "获取仪表盘总览", description = "聚合展示系统各模块的统计数据")
    public DashboardSummaryDto getSummary() {
        return dashboardService.getSummary();
    }
}
