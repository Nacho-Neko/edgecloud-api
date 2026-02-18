package com.mikou.edgecloud.account_admin.domain;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 角色状态枚举
 * 角色有启用、停用、已删除三个状态（只能标记删除，不能真删除）
 */
public enum RoleStatus {
    /**
     * 启用
     */
    ENABLED(1, "启用"),

    /**
     * 停用
     */
    DISABLED(2, "停用"),

    /**
     * 已删除（软删除，只能标记删除，不能真正删除）
     */
    DELETED(3, "已删除");

    @EnumValue  // MyBatis Plus 会将此字段的值存储到数据库
    private final int code;
    private final String displayName;

    RoleStatus(int code, String displayName) {
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
