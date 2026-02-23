package com.mikou.edgecloud.business.application;

import com.mikou.edgecloud.business.domain.product.ProductBilling;
import com.mikou.edgecloud.business.domain.product.ProductLifecycle;
import com.mikou.edgecloud.business.domain.product.ProductQuery;
import com.mikou.edgecloud.business.domain.product.ProductResource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 产品能力定位器
 * 根据业务代码查找对应的产品能力实现
 * 
 * 命名规范：
 * - 生命周期：{BusinessCode}_ProductLifecycle
 * - 查询：{BusinessCode}_ProductQuery
 * - 计费：{BusinessCode}_ProductBilling
 * - 资源：{BusinessCode}_ProductResource
 * 
 * 示例：
 * - EOP_ProductLifecycle
 * - EOP_ProductQuery
 * - EOP_ProductBilling
 * - EOP_ProductResource
 */
@Component
public class ProductCapabilityLocator {
    
    private final ApplicationContext context;
    
    public ProductCapabilityLocator(ApplicationContext context) {
        this.context = context;
    }
    
    /**
     * 获取产品生命周期管理能力
     * @param businessCode 业务代码，如 "EOP"
     * @return ProductLifecycle 实现，如果不存在返回 null
     */
    public ProductLifecycle getLifecycle(String businessCode) {
        try {
            return context.getBean(businessCode + "_ProductLifecycle", ProductLifecycle.class);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 获取产品查询能力
     * @param businessCode 业务代码
     * @return ProductQuery 实现，如果不存在返回 null
     */
    public ProductQuery getQuery(String businessCode) {
        try {
            return context.getBean(businessCode + "_ProductQuery", ProductQuery.class);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 获取产品计费能力
     * @param businessCode 业务代码
     * @return ProductBilling 实现，如果不存在返回 null
     */
    public ProductBilling getBilling(String businessCode) {
        try {
            return context.getBean(businessCode + "_ProductBilling", ProductBilling.class);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 获取产品资源管理能力
     * @param businessCode 业务代码
     * @return ProductResource 实现，如果不存在返回 null
     */
    public ProductResource getResource(String businessCode) {
        try {
            return context.getBean(businessCode + "_ProductResource", ProductResource.class);
        } catch (Exception e) {
            return null;
        }
    }
}
