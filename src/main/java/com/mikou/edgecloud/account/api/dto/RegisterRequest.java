package com.mikou.edgecloud.account.api.dto;


import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
public class RegisterRequest {
    private String username;
    private String email;
    private String password;

    public String getUsername() {
        return username;
    }

    public RegisterRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public RegisterRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
