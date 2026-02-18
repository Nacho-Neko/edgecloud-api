package com.mikou.edgecloud.business.application;

import com.mikou.edgecloud.business.domain.BusinessStatus;
import org.springframework.stereotype.Service;

/**
 * 业务状态管理服务（内部服务）
 * 未来可用于动态修改业务状态（运行中/维护中/停用）
 */
@Service
public class BusinessManagementService {
    
    private final BusinessAggregateService businessAggregateService;
    
    public BusinessManagementService(BusinessAggregateService businessAggregateService) {
        this.businessAggregateService = businessAggregateService;
    }
    
    /**
     * 检查业务是否可用（运行中）
     */
    public boolean isBusinessAvailable(String businessCode) {
        var business = businessAggregateService.getBusinessByCode(businessCode);
        return business != null && business.getStatus() == BusinessStatus.RUNNING;
    }
    
    /**
     * 检查业务是否在维护中
     */
    public boolean isBusinessUnderMaintenance(String businessCode) {
        var business = businessAggregateService.getBusinessByCode(businessCode);
        return business != null && business.getStatus() == BusinessStatus.MAINTENANCE;
    }
    
    /**
     * 检查业务是否已停用
     */
    public boolean isBusinessDisabled(String businessCode) {
        var business = businessAggregateService.getBusinessByCode(businessCode);
        return business != null && business.getStatus() == BusinessStatus.DISABLED;
    }
    
    /**
     * 获取业务状态
     */
    public BusinessStatus getBusinessStatus(String businessCode) {
        var business = businessAggregateService.getBusinessByCode(businessCode);
        return business != null ? business.getStatus() : null;
    }
    
    /**
     * 验证业务访问权限（检查业务是否可用）
     * @return true 如果业务可访问，false 如果不可访问
     */
    public boolean validateBusinessAccess(String businessCode) {
        return isBusinessAvailable(businessCode);
    }
    
    /**
     * 获取不可用原因
     */
    public String getUnavailableReason(String businessCode) {
        var business = businessAggregateService.getBusinessByCode(businessCode);
        
        if (business == null) {
            return "业务不存在";
        }
        
        switch (business.getStatus()) {
            case MAINTENANCE:
                return "业务维护中，暂时无法访问";
            case DISABLED:
                return "业务已停用";
            case RUNNING:
                return null; // 正常运行
            default:
                return "业务状态未知";
        }
    }
    
    // TODO: 未来可以添加动态修改业务状态的方法
    // public void setBusinessStatus(String businessCode, BusinessStatus status) { ... }
}
