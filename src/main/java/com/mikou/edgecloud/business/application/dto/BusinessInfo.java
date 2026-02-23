package com.mikou.edgecloud.business.application.dto;

import com.mikou.edgecloud.business.domain.BusinessStatistics;

/**
 * 业务信息
 */
public class BusinessInfo {
    private String code;
    private String name;
    private String status;
    private String statusDisplayName;
    private BusinessStatistics statistics;
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getStatusDisplayName() {
        return statusDisplayName;
    }
    
    public void setStatusDisplayName(String statusDisplayName) {
        this.statusDisplayName = statusDisplayName;
    }
    
    public BusinessStatistics getStatistics() {
        return statistics;
    }
    
    public void setStatistics(BusinessStatistics statistics) {
        this.statistics = statistics;
    }
}
