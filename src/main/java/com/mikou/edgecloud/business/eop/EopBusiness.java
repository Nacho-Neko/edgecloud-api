package com.mikou.edgecloud.business.eop;

import com.mikou.edgecloud.business.domain.BusinessDomain;
import com.mikou.edgecloud.business.domain.BusinessStatistics;
import com.mikou.edgecloud.business.domain.BusinessStatus;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * EOP（边缘开放平台）业务实现
 * 只负责业务标识和统计，具体产品能力由独立的实现类提供：
 * - EopProductLifecycle：生命周期管理
 * - EopProductQuery：产品查询
 * - EopProductBilling：计费相关
 * - EopProductResource：资源管理
 */
@Component
public class EopBusiness implements  BusinessDomain {
    
    @Override
    public String getBusinessCode() {
        return "EOP";
    }
    
    @Override
    public String getBusinessName() {
        return "边缘转发";
    }
    
    @Override
    public String getDescription() {
        return "边缘开放平台，提供边缘节点转发服务";
    }
    
    @Override
    public BusinessStatus getStatus() {
        // TODO: 从配置或数据库读取实际状态
        return BusinessStatus.ENABLED;
    }
    
    @Override
    public BusinessStatistics getStatistics() {
        BusinessStatistics stats = new BusinessStatistics();
        
        // TODO: 从实际数据源获取统计数据
        stats.setActiveProducts(0L);
        stats.setMonthlyNewProducts(0L);
        
        return stats;
    }
    
    @Override
    public List<String> getSupportedRegions() {
        return List.of("CN-ALL");
    }
}
