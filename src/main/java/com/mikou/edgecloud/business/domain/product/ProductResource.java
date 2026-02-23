package com.mikou.edgecloud.business.domain.product;

import java.util.Map;

/**
 * 产品资源管理接口
 */
public interface ProductResource {
    
    /**
     * 分配资源（在产品激活时调用）
     * @param productId 产品ID
     * @throws IllegalStateException 资源不足
     */
    void allocateResources(String productId);
    
    /**
     * 释放资源（在产品删除或过期时调用）
     * @param productId 产品ID
     */
    void releaseResources(String productId);
    
    /**
     * 检查资源可用性
     * @param params 资源需求参数
     * @return true 如果资源充足
     */
    boolean checkResourceAvailability(Map<String, Object> params);
}
