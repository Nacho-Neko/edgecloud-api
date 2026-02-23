package com.mikou.edgecloud.business.domain;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 业务状态枚举
 * 业务只有启用和停用两个状态
 */
public enum BusinessStatus {
    /**
     * 启用
     */
    ENABLED(1, "启用"),
    
    /**
     * 停用
     */
    DISABLED(2, "停用");
    
    @EnumValue
    private final int code;
    private final String displayName;
    
    BusinessStatus(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
