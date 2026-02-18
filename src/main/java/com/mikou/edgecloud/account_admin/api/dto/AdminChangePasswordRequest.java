package com.mikou.edgecloud.account_admin.api.dto;

public class AdminChangePasswordRequest {
    private String username;
    private String newPassword;

    public String getUsername() {
        return username;
    }

    public AdminChangePasswordRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public AdminChangePasswordRequest setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }
}
