package com.mikou.edgecloud.account_admin.api.dto;

/**
 * 管理员 Token 验证响应
 */
public class AdminValidateResponse {
    private String adminId;
    private String username;
    private String role;
    private String roleDisplayName;

    public String getAdminId() {
        return adminId;
    }

    public AdminValidateResponse setAdminId(String adminId) {
        this.adminId = adminId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AdminValidateResponse setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getRole() {
        return role;
    }

    public AdminValidateResponse setRole(String role) {
        this.role = role;
        return this;
    }

    public String getRoleDisplayName() {
        return roleDisplayName;
    }

    public AdminValidateResponse setRoleDisplayName(String roleDisplayName) {
        this.roleDisplayName = roleDisplayName;
        return this;
    }
}
