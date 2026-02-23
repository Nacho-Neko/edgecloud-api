package com.mikou.edgecloud.business.domain.product;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 产品计费接口
 */
public interface ProductBilling {
    
    /**
     * 计算产品价格
     * @param params 计价参数（规格、时长等）
     * @return 价格（分）
     */
    BigDecimal calculatePrice(Map<String, Object> params);
    
    /**
     * 续费产品
     * @param productId 产品ID
     * @param period 续费时长（天数）
     * @return 续费金额（分）
     */
    BigDecimal renewProduct(String productId, int period);
    
    /**
     * 获取产品用量数据（用于计费）
     * @param productId 产品ID
     * @param startTime 开始时间（Unix 时间戳，秒）
     * @param endTime 结束时间（Unix 时间戳，秒）
     * @return 用量数据（流量、请求数等，业务特定）
     */
    Map<String, Object> getUsageData(String productId, long startTime, long endTime);
}
