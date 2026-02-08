package com.mikou.edgecloud.account.api.admin.dto;

import java.time.Instant;
import java.util.UUID;

public class AdminKycMaskedDto {
    private UUID kycId;
    private UUID accountId;
    private String provider;
    private String kycType;
    private String status;

    // Masked fields (default safe view)
    private String realNameMasked;
    private String idNumberMasked;

    private Instant submittedAt;
    private Instant verifiedAt;

    public UUID getKycId() {
        return kycId;
    }

    public AdminKycMaskedDto setKycId(UUID kycId) {
        this.kycId = kycId;
        return this;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public AdminKycMaskedDto setAccountId(UUID accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getProvider() {
        return provider;
    }

    public AdminKycMaskedDto setProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public String getKycType() {
        return kycType;
    }

    public AdminKycMaskedDto setKycType(String kycType) {
        this.kycType = kycType;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public AdminKycMaskedDto setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getRealNameMasked() {
        return realNameMasked;
    }

    public AdminKycMaskedDto setRealNameMasked(String realNameMasked) {
        this.realNameMasked = realNameMasked;
        return this;
    }

    public String getIdNumberMasked() {
        return idNumberMasked;
    }

    public AdminKycMaskedDto setIdNumberMasked(String idNumberMasked) {
        this.idNumberMasked = idNumberMasked;
        return this;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public AdminKycMaskedDto setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
        return this;
    }

    public Instant getVerifiedAt() {
        return verifiedAt;
    }

    public AdminKycMaskedDto setVerifiedAt(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
        return this;
    }
}