package com.mikou.edgecloud.business.domain;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 产品状态枚举
 * 用于表示产品实例的生命周期状态
 */
public enum BusinessStatus {
    /**
     * 激活 - 产品正常运行中
     */
    ACTIVE(1, "激活"),
    
    /**
     * 暂停 - 产品被暂停（如欠费、手动暂停等）
     */
    SUSPENDED(2, "暂停"),
    
    /**
     * 过期 - 产品已过期
     */
    EXPIRED(3, "过期");
    
    @EnumValue
    @JsonValue
    private final Integer id;
    
    private final String displayName;
    
    BusinessStatus(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
