package com.mikou.edgecloud.platform.application.dashboard;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.account.domain.AccountStatus;
import com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountEntity;
import com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountKycEntity;
import com.mikou.edgecloud.account.infrastructure.persistence.mapper.AccountKycMapper;
import com.mikou.edgecloud.account.infrastructure.persistence.mapper.AccountMapper;
import com.mikou.edgecloud.account_admin.domain.RoleStatus;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.entity.AdminEntity;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.entity.AdminRoleEntity;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.mapper.AdminMapper;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.mapper.AdminRoleMapper;
import com.mikou.edgecloud.business.application.BusinessAggregateService;
import com.mikou.edgecloud.business.application.BusinessQueryService;
import com.mikou.edgecloud.platform.api.dto.DashboardSummaryDto;
import com.mikou.edgecloud.platform.api.dto.BusinessOverviewDto;
import com.mikou.edgecloud.platform.api.dto.BusinessDetailDto;
import com.mikou.edgecloud.platform.api.dto.EdgeStatisticsDto;
import com.mikou.edgecloud.platform.api.dto.AccountStatisticsDto;
import com.mikou.edgecloud.platform.api.dto.AdminStatisticsDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Dashboard 服务 - 聚合各领域数据
 */
@Service
public class DashboardService {
    
    private final AccountMapper accountMapper;
    private final AccountKycMapper accountKycMapper;
    private final AdminMapper adminMapper;
    private final AdminRoleMapper adminRoleMapper;
    private final BusinessAggregateService businessAggregateService;
    private final BusinessQueryService businessQueryService;
    // TODO: 注入 EdgeMapper 等
    
    public DashboardService(
            AccountMapper accountMapper,
            AccountKycMapper accountKycMapper,
            AdminMapper adminMapper,
            AdminRoleMapper adminRoleMapper,
            BusinessAggregateService businessAggregateService,
            BusinessQueryService businessQueryService
    ) {
        this.accountMapper = accountMapper;
        this.accountKycMapper = accountKycMapper;
        this.adminMapper = adminMapper;
        this.adminRoleMapper = adminRoleMapper;
        this.businessAggregateService = businessAggregateService;
        this.businessQueryService = businessQueryService;
    }
    
    /**
     * 获取 Dashboard 总览数据
     */
    public DashboardSummaryDto getSummary() {
        DashboardSummaryDto summary = new DashboardSummaryDto();
        
        // 聚合业务总览统计
        summary.setBusinessOverview(getBusinessOverview());
        
        // 聚合各业务详细统计（通过 Business 领域）
        summary.setBusinessDetails(getBusinessDetails());
        
        // 聚合边缘节点统计
        summary.setEdgeStatistics(getEdgeStatistics());

        // 聚合账户统计
        summary.setAccountStatistics(getAccountStatistics());
        
        // 聚合管理员统计
        summary.setAdminStatistics(getAdminStatistics());
        
        return summary;
    }
    
    /**
     * 获取业务总览统计
     */
    private BusinessOverviewDto getBusinessOverview() {
        BusinessOverviewDto overview = new BusinessOverviewDto();
        
        overview.setTotalBusinesses((long) businessAggregateService.getAllBusinesses().size());
        overview.setRunningBusinesses(businessAggregateService.getRunningBusinessCount());
        overview.setActiveProducts(businessAggregateService.getTotalActiveProducts());
        overview.setMonthlyNewProducts(businessAggregateService.getTotalMonthlyNewProducts());
        
        return overview;
    }
    
    /**
     * 获取各业务详细统计（通过 Business 领域聚合）
     */
    private Map<String, BusinessDetailDto> getBusinessDetails() {
        Map<String, BusinessDetailDto> details = new HashMap<>();
        
        // 通过 Business 领域获取所有业务的信息
        var allBusinessInfo = businessQueryService.getAllBusinessInfo();
        
        for (var businessInfo : allBusinessInfo) {
            BusinessDetailDto detail = new BusinessDetailDto();
            detail.setCode(businessInfo.getCode());
            detail.setName(businessInfo.getName());
            detail.setStatus(businessInfo.getStatus());
            
            var stats = businessInfo.getStatistics();
            if (stats != null) {
                detail.setActiveProducts(stats.getActiveProducts());
                detail.setMonthlyNewProducts(stats.getMonthlyNewProducts());
            }
            
            details.put(businessInfo.getCode(), detail);
        }
        
        return details;
    }
    
    /**
     * 获取边缘节点统计
     */
    private EdgeStatisticsDto getEdgeStatistics() {
        EdgeStatisticsDto stats = new EdgeStatisticsDto();
        
        // TODO: 等 edge 领域的 Mapper 可用后实现
        // Long totalNodes = edgeMapper.selectCount(null);
        // Long onlineNodes = edgeMapper.selectCount(
        //     new LambdaQueryWrapper<EdgeEntity>()
        //         .eq(EdgeEntity::getStatus, EdgeStatus.ONLINE)
        // );
        
        stats.setTotalNodes(0L);
        stats.setOnlineNodes(0L);
        stats.setOfflineNodes(0L);
        stats.setTotalRegions(0L);
        
        return stats;
    }
    
    /**
     * 获取账户统计
     */
    private AccountStatisticsDto getAccountStatistics() {
        AccountStatisticsDto stats = new AccountStatisticsDto();
        
        // 总账户数
        Long totalAccounts = accountMapper.selectCount(null);
        stats.setTotalAccounts(totalAccounts);
        
        // 激活账户数
        Long activeAccounts = accountMapper.selectCount(
            new LambdaQueryWrapper<AccountEntity>()
                .eq(AccountEntity::getStatus, "ACTIVE")
        );
        stats.setActiveAccounts(activeAccounts);
        
        // 总 KYC 数
        Long totalKyc = accountKycMapper.selectCount(null);
        stats.setTotalKyc(totalKyc);
        
        // 已验证 KYC 数
        Long verifiedKyc = accountKycMapper.selectCount(
            new LambdaQueryWrapper<AccountKycEntity>()
                .eq(AccountKycEntity::getStatus, "VERIFIED")
        );
        stats.setVerifiedKyc(verifiedKyc);
        
        return stats;
    }
    
    /**
     * 获取管理员统计
     */
    private AdminStatisticsDto getAdminStatistics() {
        AdminStatisticsDto stats = new AdminStatisticsDto();
        
        // 总管理员数
        Long totalAdmins = adminMapper.selectCount(null);
        stats.setTotalAdmins(totalAdmins);
        
        // 激活管理员数
        Long activeAdmins = adminMapper.selectCount(
            new LambdaQueryWrapper<AdminEntity>()
                .eq(AdminEntity::getAccountStatus, AccountStatus.ENABLED)
        );
        stats.setActiveAdmins(activeAdmins);
        
        // 总角色数
        Long totalRoles = adminRoleMapper.selectCount(
            new LambdaQueryWrapper<AdminRoleEntity>()
                .eq(AdminRoleEntity::getStatus, RoleStatus.ENABLED)
        );
        stats.setTotalRoles(totalRoles);
        
        return stats;
    }
}