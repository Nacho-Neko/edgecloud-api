package com.mikou.edgecloud.business.application;

import com.mikou.edgecloud.business.domain.BusinessDomain;
import com.mikou.edgecloud.business.domain.BusinessStatistics;
import com.mikou.edgecloud.business.domain.BusinessStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务聚合服务（内部服务，不对外提供 API）
 * 供 Platform、Dashboard 等其他领域调用
 * Spring 会自动注入所有 BusinessDomain 的实现类
 */
@Service
public class BusinessAggregateService {
    
    private final List<BusinessDomain> businessDomains;
    
    public BusinessAggregateService(List<BusinessDomain> businessDomains) {
        this.businessDomains = businessDomains;
    }
    
    /**
     * 获取所有业务领域
     */
    public List<BusinessDomain> getAllBusinesses() {
        return businessDomains;
    }
    
    /**
     * 获取所有业务代码列表
     */
    public List<String> getAllBusinessCodes() {
        return businessDomains.stream()
                .map(BusinessDomain::getBusinessCode)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取业务总数
     */
    public long getTotalBusinessCount() {
        return businessDomains.size();
    }
    
    /**
     * 获取运行中的业务数量
     */
    public long getRunningBusinessCount() {
        return businessDomains.stream()
                .filter(b -> b.getStatus() == BusinessStatus.ENABLED)
                .count();
    }
    
    /**
     * 获取所有业务的激活产品总数
     */
    public long getTotalActiveProducts() {
        return businessDomains.stream()
                .mapToLong(b -> {
                    Long products = b.getStatistics().getActiveProducts();
                    return products != null ? products : 0L;
                })
                .sum();
    }
    
    /**
     * 获取所有业务本月新增产品总数
     */
    public long getTotalMonthlyNewProducts() {
        return businessDomains.stream()
                .mapToLong(b -> {
                    Long products = b.getStatistics().getMonthlyNewProducts();
                    return products != null ? products : 0L;
                })
                .sum();
    }
    
    /**
     * 根据业务代码获取业务
     */
    public BusinessDomain getBusinessByCode(String code) {
        return businessDomains.stream()
                .filter(b -> b.getBusinessCode().equals(code))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 检查业务是否存在
     */
    public boolean businessExists(String code) {
        return businessDomains.stream()
                .anyMatch(b -> b.getBusinessCode().equals(code));
    }
    
    /**
     * 获取指定状态的业务列表
     */
    public List<BusinessDomain> getBusinessesByStatus(BusinessStatus status) {
        return businessDomains.stream()
                .filter(b -> b.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取所有运行中的业务
     */
    public List<BusinessDomain> getRunningBusinesses() {
        return getBusinessesByStatus(BusinessStatus.ENABLED);
    }
    
    /**
     * 获取所有业务的统计信息映射（业务代码 -> 统计数据）
     */
    public Map<String, BusinessStatistics> getAllBusinessStatistics() {
        return businessDomains.stream()
                .collect(Collectors.toMap(
                        BusinessDomain::getBusinessCode,
                        BusinessDomain::getStatistics
                ));
    }
    
    /**
     * 获取业务状态分布
     */
    public Map<BusinessStatus, Long> getBusinessStatusDistribution() {
        return businessDomains.stream()
                .collect(Collectors.groupingBy(
                        BusinessDomain::getStatus,
                        Collectors.counting()
                ));
    }
}
