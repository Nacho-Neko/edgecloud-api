package com.mikou.edgecloud.account.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.Instant;
import java.util.UUID;

@TableName("account_kyc")
public class AccountKycEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private UUID id;

    @TableField("account_id")
    private UUID accountId;

    @TableField("provider")
    private String provider;

    @TableField("kyc_type")
    private String kycType;

    @TableField("status")
    private String status;

    @TableField("external_request_id")
    private String externalRequestId;

    @TableField("external_reference_id")
    private String externalReferenceId;

    @TableField("submitted_at")
    private Instant submittedAt;

    @TableField("verified_at")
    private Instant verifiedAt;

    @TableField("created_at")
    private Instant createdAt;

    @TableField("updated_at")
    private Instant updatedAt;

    public UUID getId() {
        return id;
    }

    public AccountKycEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public AccountKycEntity setAccountId(UUID accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getProvider() {
        return provider;
    }

    public AccountKycEntity setProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public String getKycType() {
        return kycType;
    }

    public AccountKycEntity setKycType(String kycType) {
        this.kycType = kycType;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public AccountKycEntity setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getExternalRequestId() {
        return externalRequestId;
    }

    public AccountKycEntity setExternalRequestId(String externalRequestId) {
        this.externalRequestId = externalRequestId;
        return this;
    }

    public String getExternalReferenceId() {
        return externalReferenceId;
    }

    public AccountKycEntity setExternalReferenceId(String externalReferenceId) {
        this.externalReferenceId = externalReferenceId;
        return this;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public AccountKycEntity setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
        return this;
    }

    public Instant getVerifiedAt() {
        return verifiedAt;
    }

    public AccountKycEntity setVerifiedAt(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AccountKycEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public AccountKycEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
