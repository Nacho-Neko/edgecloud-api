package com.mikou.edgecloud.account_admin.api.dto;

public class AdminLoginRequest {
    private String usernameOrEmail;
    private String password;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public AdminLoginRequest setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AdminLoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
