package com.mikou.edgecloud.business.application;

import com.mikou.edgecloud.business.api.dto.BusinessCapabilityDto;
import com.mikou.edgecloud.business.domain.BusinessCapability;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务能力注册中心
 * 通过 Spring 自动发现并注册所有实现了 BusinessCapability 接口的业务模块
 */
@Service
public class BusinessCapabilityRegistry {

    private final List<BusinessCapability> capabilities;

    /**
     * Spring 自动注入所有实现了 BusinessCapability 接口的 Bean
     */
    public BusinessCapabilityRegistry(List<BusinessCapability> capabilities) {
        this.capabilities = capabilities;
    }

    /**
     * 获取所有已注册的业务能力
     */
    public List<BusinessCapabilityDto> getCapabilities() {
        return capabilities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 获取已启用的业务能力
     */
    public List<BusinessCapabilityDto> getEnabledCapabilities() {
        return capabilities.stream()
                .filter(BusinessCapability::isEnabled)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 检查指定能力是否已注册
     */
    public boolean hasCapability(String code) {
        return capabilities.stream()
                .anyMatch(cap -> cap.getCode().equalsIgnoreCase(code));
    }

    /**
     * 获取指定能力信息
     */
    public BusinessCapabilityDto getCapability(String code) {
        return capabilities.stream()
                .filter(cap -> cap.getCode().equalsIgnoreCase(code))
                .findFirst()
                .map(this::toDto)
                .orElse(null);
    }

    /**
     * 转换为 DTO
     */
    private BusinessCapabilityDto toDto(BusinessCapability capability) {
        return new BusinessCapabilityDto()
                .setCode(capability.getCode())
                .setName(capability.getName())
                .setDescription(capability.getDescription())
                .setEnabled(capability.isEnabled())
                .setVersion(capability.getVersion());
    }
}
