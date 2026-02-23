package com.mikou.edgecloud.business.eop.product;

import com.mikou.edgecloud.business.domain.ProductStatus;
import com.mikou.edgecloud.business.domain.product.ProductQuery;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * EOP 产品查询实现
 * 业务代码：EOP
 */
@Component("EOP_ProductQuery")
public class EopProductQuery implements ProductQuery {
    
    @Override
    public Object getProduct(String productId) {
        // TODO: 实现产品查询
        // 1. 从数据库查询产品
        // 2. 转换为 DTO 返回
        throw new UnsupportedOperationException("EOP 产品查询功能待实现");
    }
    
    @Override
    public List<Object> listProducts(String accountId) {
        // TODO: 实现产品列表查询
        // 1. 从数据库查询该账户的所有产品
        // 2. 转换为 DTO 列表返回
        throw new UnsupportedOperationException("EOP 产品列表查询功能待实现");
    }
    
    @Override
    public ProductStatus getProductStatus(String productId) {
        // TODO: 实现产品状态查询
        // 1. 从数据库查询产品状态
        // 2. 返回状态枚举
        throw new UnsupportedOperationException("EOP 产品状态查询功能待实现");
    }
}