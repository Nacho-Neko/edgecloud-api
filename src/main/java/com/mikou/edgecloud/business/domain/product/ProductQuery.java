package com.mikou.edgecloud.business.domain.product;

import com.mikou.edgecloud.business.domain.ProductStatus;

import java.util.List;

/**
 * 产品查询接口
 */
public interface ProductQuery {
    
    /**
     * 查询单个产品
     * @param productId 产品ID
     * @return 产品信息（业务特定的产品对象）
     */
    Object getProduct(String productId);
    
    /**
     * 查询账户的所有产品
     * @param accountId 账户ID
     * @return 产品列表
     */
    List<Object> listProducts(String accountId);
    
    /**
     * 查询产品状态
     * @param productId 产品ID
     * @return 产品状态
     */
    ProductStatus getProductStatus(String productId);
    
    /**
     * 统计账户的产品数量
     * @param accountId 账户ID
     * @return 产品数量
     */
    default long countProducts(String accountId) {
        return listProducts(accountId).size();
    }
}
