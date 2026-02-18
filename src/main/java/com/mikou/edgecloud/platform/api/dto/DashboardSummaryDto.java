package com.mikou.edgecloud.platform.api.dto;

import java.util.Map;

/**
 * Dashboard 总览数据
 */
public class DashboardSummaryDto {
    
    private BusinessOverviewDto businessOverview;
    private Map<String, BusinessDetailDto> businessDetails;
    private EdgeStatisticsDto edgeStatistics;
    private AccountStatisticsDto accountStatistics;
    private AdminStatisticsDto adminStatistics;
    
    public BusinessOverviewDto getBusinessOverview() {
        return businessOverview;
    }
    
    public DashboardSummaryDto setBusinessOverview(BusinessOverviewDto businessOverview) {
        this.businessOverview = businessOverview;
        return this;
    }
    
    public Map<String, BusinessDetailDto> getBusinessDetails() {
        return businessDetails;
    }
    
    public DashboardSummaryDto setBusinessDetails(Map<String, BusinessDetailDto> businessDetails) {
        this.businessDetails = businessDetails;
        return this;
    }
    
    public EdgeStatisticsDto getEdgeStatistics() {
        return edgeStatistics;
    }
    
    public DashboardSummaryDto setEdgeStatistics(EdgeStatisticsDto edgeStatistics) {
        this.edgeStatistics = edgeStatistics;
        return this;
    }
    
    public AccountStatisticsDto getAccountStatistics() {
        return accountStatistics;
    }
    
    public DashboardSummaryDto setAccountStatistics(AccountStatisticsDto accountStatistics) {
        this.accountStatistics = accountStatistics;
        return this;
    }
    
    public AdminStatisticsDto getAdminStatistics() {
        return adminStatistics;
    }
    
    public DashboardSummaryDto setAdminStatistics(AdminStatisticsDto adminStatistics) {
        this.adminStatistics = adminStatistics;
        return this;
    }
}
