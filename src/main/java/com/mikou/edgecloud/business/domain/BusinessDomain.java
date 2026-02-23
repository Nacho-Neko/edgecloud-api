package com.mikou.edgecloud.business.domain;

import java.util.List;

/**
 * 业务领域接口
 * 所有业务子领域必须实现此接口，用于业务识别和统计
 * 
 * 职责：
 * - 业务元信息（代码、名称）
 * - 业务状态管理
 * - 业务级统计数据
 * 
 * 使用场景：Platform、Dashboard 用于聚合展示所有业务信息
 */
public interface BusinessDomain {
    
    /**
     * 获取业务代码（唯一标识）
     * @return 业务代码，如 "EOP"、"STORAGE"、"CDN"
     */
    String getBusinessCode();
    
    /**
     * 获取业务名称（显示名称）
     * @return 业务名称，如 "边缘转发"、"云存储"、"内容分发"
     */
    String getBusinessName();
    
    /**
     * 获取业务当前状态
     * @return 业务状态（ENABLED、DISABLED）
     */
    BusinessStatus getStatus();
    
    /**
     * 获取业务统计数据
     * @return 业务统计信息（用户数、订单数、收入等）
     */
    BusinessStatistics getStatistics();
    
    /**
     * 获取业务支持的地区列表
     * @return 地区代码列表，如 ["CN-BJ", "CN-SH", "US-WEST"]
     */
    default List<String> getSupportedRegions() {
        return List.of(); // 默认不限制地区
    }
    
    /**
     * 获取业务描述
     * @return 业务描述文本
     */
    default String getDescription() {
        return "";
    }
}
