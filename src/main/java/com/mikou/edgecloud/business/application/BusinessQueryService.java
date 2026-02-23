package com.mikou.edgecloud.business.application;

import com.mikou.edgecloud.business.application.dto.BusinessInfo;
import com.mikou.edgecloud.business.application.dto.BusinessProductRank;
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
     * 获取业务产品数量排名（Top N）
     */
    public List<BusinessProductRank> getProductRanking(int topN) {
        return businessAggregateService.getAllBusinesses().stream()
                .sorted((b1, b2) -> {
                    Long p1 = b1.getStatistics().getActiveProducts();
                    Long p2 = b2.getStatistics().getActiveProducts();
                    return Long.compare(
                            p2 != null ? p2 : 0L,
                            p1 != null ? p1 : 0L
                    );
                })
                .limit(topN)
                .map(b -> {
                    BusinessProductRank rank = new BusinessProductRank();
                    rank.setBusinessCode(b.getBusinessCode());
                    rank.setBusinessName(b.getBusinessName());
                    rank.setTotalProducts(b.getStatistics().getActiveProducts());
                    rank.setActiveProducts(b.getStatistics().getActiveProducts());
                    return rank;
                })
                .collect(Collectors.toList());
    }
}
