package com.mikou.edgecloud.platform.api.dto;

/**
 * 账户统计
 */
public class AccountStatisticsDto {
    private Long totalAccounts;
    private Long activeAccounts;
    private Long totalKyc;
    private Long verifiedKyc;
    
    public Long getTotalAccounts() {
        return totalAccounts;
    }
    
    public AccountStatisticsDto setTotalAccounts(Long totalAccounts) {
        this.totalAccounts = totalAccounts;
        return this;
    }
    
    public Long getActiveAccounts() {
        return activeAccounts;
    }
    
    public AccountStatisticsDto setActiveAccounts(Long activeAccounts) {
        this.activeAccounts = activeAccounts;
        return this;
    }
    
    public Long getTotalKyc() {
        return totalKyc;
    }
    
    public AccountStatisticsDto setTotalKyc(Long totalKyc) {
        this.totalKyc = totalKyc;
        return this;
    }
    
    public Long getVerifiedKyc() {
        return verifiedKyc;
    }
    
    public AccountStatisticsDto setVerifiedKyc(Long verifiedKyc) {
        this.verifiedKyc = verifiedKyc;
        return this;
    }
}
