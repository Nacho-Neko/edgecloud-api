package com.mikou.edgecloud.account.api.dto;

public class LoginResponse {
    private String tokenType;
    private String accessToken;

    public String getTokenType() {
        return tokenType;
    }

    public LoginResponse setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public LoginResponse setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }
}
