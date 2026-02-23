package com.mikou.edgecloud.business.application;

import com.mikou.edgecloud.business.domain.BusinessDomain;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品业务注册中心
 * 管理所有 ProductBusiness 实现，提供统一的访问入口
 * 
 * 使用方式：
 * <pre>
 * ProductBusiness business = registry.getByCode("EOP");
 * business.createProduct(accountId, params);
 * </pre>
 */
@Component
public class ProductBusinessRegistry {
    
    private final Map<String, BusinessDomain> businessMap;
    
    /**
     * Spring 自动注入所有 ProductBusiness 实现
     */
    public ProductBusinessRegistry(List<BusinessDomain> businesses) {
        this.businessMap = new HashMap<>();
        for (BusinessDomain business : businesses) {
            businessMap.put(business.getBusinessCode(), business);
        }
    }
    
    /**
     * 根据业务代码获取业务实现
     * @param businessCode 业务代码（如 "EOP"）
     * @return ProductBusiness 实现
     * @throws IllegalArgumentException 如果业务代码不存在
     */
    public BusinessDomain getByCode(String businessCode) {
        BusinessDomain business = businessMap.get(businessCode);
        if (business == null) {
            throw new IllegalArgumentException("Business not found: " + businessCode);
        }
        return business;
    }
    
    /**
     * 检查业务代码是否存在
     * @param businessCode 业务代码
     * @return true 如果业务存在
     */
    public boolean exists(String businessCode) {
        return businessMap.containsKey(businessCode);
    }
    
    /**
     * 获取所有业务代码
     * @return 业务代码列表
     */
    public List<String> getAllBusinessCodes() {
        return List.copyOf(businessMap.keySet());
    }
    
    /**
     * 获取所有业务实现
     * @return ProductBusiness 列表
     */
    public List<BusinessDomain> getAllBusinesses() {
        return List.copyOf(businessMap.values());
    }
}
