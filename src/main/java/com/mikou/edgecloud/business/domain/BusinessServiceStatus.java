package com.mikou.edgecloud.business.domain;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 业务服务状态枚举（通用）
 * 适用于所有业务子产品的服务实例状态
 */
public enum BusinessServiceStatus {
    /**
     * 未激活 - 服务已创建但未激活
     */
    INACTIVE(1, "未激活"),
    
    /**
     * 准备中 - 服务正在准备中（如 EOP 的 OUTBOUND_READY）
     */
    PREPARING(2, "准备中"),
    
    /**
     * 激活 - 服务正常运行
     */
    ACTIVE(3, "激活"),
    
    /**
     * 暂停 - 服务被暂停（如到期未续费）
     */
    SUSPENDED(4, "暂停");

    @EnumValue
    @JsonValue
    private final int id;
    
    private final String displayName;

    BusinessServiceStatus(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
