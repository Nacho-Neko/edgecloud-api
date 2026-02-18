package com.mikou.edgecloud.business.eop;

import com.mikou.edgecloud.business.domain.BusinessDomain;
import com.mikou.edgecloud.business.domain.BusinessStatistics;
import com.mikou.edgecloud.business.domain.BusinessStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * EOP（边缘开放平台）业务领域实现
 */
@Component
public class EopBusinessDomain implements BusinessDomain {
    @Override
    public String getBusinessCode() {
        return "EOP";
    }
    
    @Override
    public String getBusinessName() {
        return "边缘转发";
    }
    
    @Override
    public BusinessStatus getStatus() {
        // TODO: 从配置或数据库读取实际状态
        return BusinessStatus.ACTIVE;
    }
    
    @Override
    public BusinessStatistics getStatistics() {
        BusinessStatistics stats = new BusinessStatistics();

        stats.setTotalUsers(0L);
        stats.setActiveUsers(0L);
        stats.setTotalOrders(0L);
        stats.setTotalRevenue(0L);

        // EOP 特有的统计指标
        Map<String, Object> customMetrics = new HashMap<>();

        customMetrics.put("totalProducts", 0);
        customMetrics.put("totalServices", 0);
        customMetrics.put("totalApps", 0);
        stats.setCustomMetrics(customMetrics);
        
        return stats;
    }
}
