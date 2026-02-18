package com.mikou.edgecloud.business.application;

import com.mikou.edgecloud.business.domain.BusinessDomain;
import com.mikou.edgecloud.business.domain.BusinessStatistics;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务查询服务（内部服务）
 * 提供各种业务数据查询功能
 */
@Service
public class BusinessQueryService {
    
    private final BusinessAggregateService businessAggregateService;
    
    public BusinessQueryService(BusinessAggregateService businessAggregateService) {
        this.businessAggregateService = businessAggregateService;
    }
    
    /**
     * 查询业务的详细信息
     */
    public BusinessInfo getBusinessInfo(String businessCode) {
        BusinessDomain business = businessAggregateService.getBusinessByCode(businessCode);
        if (business == null) {
            return null;
        }
        
        BusinessInfo info = new BusinessInfo();
        info.setCode(business.getBusinessCode());
        info.setName(business.getBusinessName());
        info.setStatus(business.getStatus().name());
        info.setStatusDisplayName(business.getStatus().getDisplayName());
        info.setStatistics(business.getStatistics());
        
        return info;
    }
    
    /**
     * 查询所有业务的简要信息
     */
    public List<BusinessInfo> getAllBusinessInfo() {
        return businessAggregateService.getAllBusinesses().stream()
                .map(b -> {
                    BusinessInfo info = new BusinessInfo();
                    info.setCode(b.getBusinessCode());
                    info.setName(b.getBusinessName());
                    info.setStatus(b.getStatus().name());
                    info.setStatusDisplayName(b.getStatus().getDisplayName());
                    info.setStatistics(b.getStatistics());
                    return info;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 获取所有业务的详细统计（按业务代码映射）
     */
    public Map<String, BusinessStatistics> getAllBusinessStatisticsMap() {
        return businessAggregateService.getAllBusinesses().stream()
                .collect(Collectors.toMap(
                        BusinessDomain::getBusinessCode,
                        BusinessDomain::getStatistics
                ));
    }
    
    /**
     * 获取业务收入排名（Top N）
     */
    public List<BusinessRevenueRank> getRevenueRanking(int topN) {
        return businessAggregateService.getAllBusinesses().stream()
                .sorted((b1, b2) -> {
                    Long r1 = b1.getStatistics().getTotalRevenue();
                    Long r2 = b2.getStatistics().getTotalRevenue();
                    return Long.compare(
                            r2 != null ? r2 : 0L,
                            r1 != null ? r1 : 0L
                    );
                })
                .limit(topN)
                .map(b -> {
                    BusinessRevenueRank rank = new BusinessRevenueRank();
                    rank.setBusinessCode(b.getBusinessCode());
                    rank.setBusinessName(b.getBusinessName());
                    rank.setRevenue(b.getStatistics().getTotalRevenue());
                    return rank;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 获取业务用户数排名（Top N）
     */
    public List<BusinessUserRank> getUserRanking(int topN) {
        return businessAggregateService.getAllBusinesses().stream()
                .sorted((b1, b2) -> {
                    Long u1 = b1.getStatistics().getTotalUsers();
                    Long u2 = b2.getStatistics().getTotalUsers();
                    return Long.compare(
                            u2 != null ? u2 : 0L,
                            u1 != null ? u1 : 0L
                    );
                })
                .limit(topN)
                .map(b -> {
                    BusinessUserRank rank = new BusinessUserRank();
                    rank.setBusinessCode(b.getBusinessCode());
                    rank.setBusinessName(b.getBusinessName());
                    rank.setTotalUsers(b.getStatistics().getTotalUsers());
                    return rank;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 业务信息封装类
     */
    public static class BusinessInfo {
        private String code;
        private String name;
        private String status;
        private String statusDisplayName;
        private BusinessStatistics statistics;
        
        // Getters and Setters
        public String getCode() {
            return code;
        }
        
        public void setCode(String code) {
            this.code = code;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public String getStatusDisplayName() {
            return statusDisplayName;
        }
        
        public void setStatusDisplayName(String statusDisplayName) {
            this.statusDisplayName = statusDisplayName;
        }
        
        public BusinessStatistics getStatistics() {
            return statistics;
        }
        
        public void setStatistics(BusinessStatistics statistics) {
            this.statistics = statistics;
        }
    }
    
    /**
     * 业务收入排名
     */
    public static class BusinessRevenueRank {
        private String businessCode;
        private String businessName;
        private Long revenue;
        
        public String getBusinessCode() {
            return businessCode;
        }
        
        public void setBusinessCode(String businessCode) {
            this.businessCode = businessCode;
        }
        
        public String getBusinessName() {
            return businessName;
        }
        
        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }
        
        public Long getRevenue() {
            return revenue;
        }
        
        public void setRevenue(Long revenue) {
            this.revenue = revenue;
        }
    }
    
    /**
     * 业务用户数排名
     */
    public static class BusinessUserRank {
        private String businessCode;
        private String businessName;
        private Long totalUsers;
        
        public String getBusinessCode() {
            return businessCode;
        }
        
        public void setBusinessCode(String businessCode) {
            this.businessCode = businessCode;
        }
        
        public String getBusinessName() {
            return businessName;
        }
        
        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }
        
        public Long getTotalUsers() {
            return totalUsers;
        }
        
        public void setTotalUsers(Long totalUsers) {
            this.totalUsers = totalUsers;
        }
    }
}
