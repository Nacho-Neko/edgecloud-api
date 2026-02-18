package com.mikou.edgecloud.account.api.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * 账户信息（包含KYC信息）
 */
public class AccountWithKycDto {

    private UUID id;
    private String username;
    private String email;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
    
    // KYC 信息
    private UUID kycId;
    private String kycProvider;
    private String kycType;
    private String kycStatus;
    private Instant kycSubmittedAt;
    private Instant kycVerifiedAt;

    public UUID getId() {
        return id;
    }

    public AccountWithKycDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AccountWithKycDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AccountWithKycDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public AccountWithKycDto setStatus(String status) {
        this.status = status;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AccountWithKycDto setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public AccountWithKycDto setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public UUID getKycId() {
        return kycId;
    }

    public AccountWithKycDto setKycId(UUID kycId) {
        this.kycId = kycId;
        return this;
    }

    public String getKycProvider() {
        return kycProvider;
    }

    public AccountWithKycDto setKycProvider(String kycProvider) {
        this.kycProvider = kycProvider;
        return this;
    }

    public String getKycType() {
        return kycType;
    }

    public AccountWithKycDto setKycType(String kycType) {
        this.kycType = kycType;
        return this;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public AccountWithKycDto setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
        return this;
    }

    public Instant getKycSubmittedAt() {
        return kycSubmittedAt;
    }

    public AccountWithKycDto setKycSubmittedAt(Instant kycSubmittedAt) {
        this.kycSubmittedAt = kycSubmittedAt;
        return this;
    }

    public Instant getKycVerifiedAt() {
        return kycVerifiedAt;
    }

    public AccountWithKycDto setKycVerifiedAt(Instant kycVerifiedAt) {
        this.kycVerifiedAt = kycVerifiedAt;
        return this;
    }
}
