package com.mikou.edgecloud.account.api.admin.dto;

import java.time.Instant;
import java.util.UUID;

public class AdminKycRevealedDto {
    private UUID kycId;
    private UUID accountId;
    private String provider;
    private String kycType;
    private String status;

    // Plain fields (privileged view)
    private String realNamePlain;
    private String idNumberPlain;

    private Instant submittedAt;
    private Instant verifiedAt;

    public UUID getKycId() {
        return kycId;
    }

    public AdminKycRevealedDto setKycId(UUID kycId) {
        this.kycId = kycId;
        return this;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public AdminKycRevealedDto setAccountId(UUID accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getProvider() {
        return provider;
    }

    public AdminKycRevealedDto setProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public String getKycType() {
        return kycType;
    }

    public AdminKycRevealedDto setKycType(String kycType) {
        this.kycType = kycType;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public AdminKycRevealedDto setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getRealNamePlain() {
        return realNamePlain;
    }

    public AdminKycRevealedDto setRealNamePlain(String realNamePlain) {
        this.realNamePlain = realNamePlain;
        return this;
    }

    public String getIdNumberPlain() {
        return idNumberPlain;
    }

    public AdminKycRevealedDto setIdNumberPlain(String idNumberPlain) {
        this.idNumberPlain = idNumberPlain;
        return this;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public AdminKycRevealedDto setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
        return this;
    }

    public Instant getVerifiedAt() {
        return verifiedAt;
    }

    public AdminKycRevealedDto setVerifiedAt(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
        return this;
    }
}
