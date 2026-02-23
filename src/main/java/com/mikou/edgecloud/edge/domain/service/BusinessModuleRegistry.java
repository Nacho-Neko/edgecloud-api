package com.mikou.edgecloud.edge.domain.service;

import com.mikou.edgecloud.edge.api.dto.BusinessModuleDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务模块注册服务
 * 用于管理和查询系统中已注册的业务模块
 */
@Service
public class BusinessModuleRegistry {

    /**
     * 获取所有已注册的业务模块
     * TODO: 后续可以改为从配置文件或数据库动态加载
     */
    public List<BusinessModuleDto> getRegisteredModules() {
        List<BusinessModuleDto> modules = new ArrayList<>();
        
        // EOP 模块
        modules.add(new BusinessModuleDto()
                .setCode("EOP")
                .setName("边缘运营平台")
                .setDescription("Edge Operation Platform - 提供边缘节点资源的商业化运营服务")
                .setEnabled(true)
                .setVersion("1.0.0"));

        return modules;
    }

    /**
     * 检查指定模块是否已注册
     */
    public boolean isModuleRegistered(String moduleCode) {
        return getRegisteredModules().stream()
                .anyMatch(module -> module.getCode().equalsIgnoreCase(moduleCode));
    }

    /**
     * 获取指定模块信息
     */
    public BusinessModuleDto getModule(String moduleCode) {
        return getRegisteredModules().stream()
                .filter(module -> module.getCode().equalsIgnoreCase(moduleCode))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取已启用的模块列表
     */
    public List<BusinessModuleDto> getEnabledModules() {
        return getRegisteredModules().stream()
                .filter(BusinessModuleDto::isEnabled)
                .toList();
    }
}
