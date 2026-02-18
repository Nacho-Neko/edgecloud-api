package com.mikou.edgecloud.account_admin.api.dto;

public class AdminLoginResponse {
    private String tokenType;
    private String accessToken;
    private String adminId;
    private String username;
    private String role;
    private String roleDisplayName;

    public String getTokenType() {
        return tokenType;
    }

    public AdminLoginResponse setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public AdminLoginResponse setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getAdminId() {
        return adminId;
    }

    public AdminLoginResponse setAdminId(String adminId) {
        this.adminId = adminId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AdminLoginResponse setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getRole() {
        return role;
    }

    public AdminLoginResponse setRole(String role) {
        this.role = role;
        return this;
    }

    public String getRoleDisplayName() {
        return roleDisplayName;
    }

    public AdminLoginResponse setRoleDisplayName(String roleDisplayName) {
        this.roleDisplayName = roleDisplayName;
        return this;
    }
}
