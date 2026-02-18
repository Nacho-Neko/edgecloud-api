package com.mikou.edgecloud.account_admin.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.UUID;

@TableName("admin_role_permission")
public class AdminRolePermissionEntity {

    @TableField("role_id")
    private UUID roleId;

    @TableField("permission_id")
    private UUID permissionId;

    public UUID getRoleId() {
        return roleId;
    }

    public AdminRolePermissionEntity setRoleId(UUID roleId) {
        this.roleId = roleId;
        return this;
    }

    public UUID getPermissionId() {
        return permissionId;
    }

    public AdminRolePermissionEntity setPermissionId(UUID permissionId) {
        this.permissionId = permissionId;
        return this;
    }
}
