package com.mikou.edgecloud.account.api.dto;

import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
public class LoginRequest {
    private String usernameOrEmail;
    private String password;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public LoginRequest setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
