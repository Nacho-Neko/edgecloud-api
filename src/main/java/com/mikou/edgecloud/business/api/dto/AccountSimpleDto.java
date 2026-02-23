package com.mikou.edgecloud.business.api.dto;

import java.util.UUID;

/**
 * 账户简单信息 DTO
 * 用于在业务服务列表中展示账户基本信息
 */
public class AccountSimpleDto {
    private UUID accountId;
    private String username;
    private String email;

    public UUID getAccountId() {
        return accountId;
    }

    public AccountSimpleDto setAccountId(UUID accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AccountSimpleDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AccountSimpleDto setEmail(String email) {
        this.email = email;
        return this;
    }
}
