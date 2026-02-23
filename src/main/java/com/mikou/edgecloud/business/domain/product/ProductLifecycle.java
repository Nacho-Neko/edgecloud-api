package com.mikou.edgecloud.business.domain.product;

import java.util.Map;

/**
 * 产品生命周期管理接口
 */
public interface ProductLifecycle {
    
    /**
     * 创建产品实例
     * @param accountId 账户ID
     * @param params 创建参数（业务特定参数）
     * @return 产品ID
     * @throws IllegalArgumentException 参数无效
     * @throws IllegalStateException 资源不足或状态不允许
     */
    String createProduct(String accountId, Map<String, Object> params);
    
    /**
     * 激活产品
     * @param productId 产品ID
     * @throws IllegalArgumentException 产品不存在
     * @throws IllegalStateException 状态不允许激活
     */
    void activateProduct(String productId);
    
    /**
     * 暂停产品（如欠费、违规）
     * @param productId 产品ID
     * @param reason 暂停原因
     */
    void suspendProduct(String productId, String reason);
    
    /**
     * 恢复产品（从暂停状态恢复）
     * @param productId 产品ID
     */
    void resumeProduct(String productId);
    
    /**
     * 过期产品
     * @param productId 产品ID
     */
    void expireProduct(String productId);
    
    /**
     * 删除产品（软删除）
     * @param productId 产品ID
     */
    void deleteProduct(String productId);
}
