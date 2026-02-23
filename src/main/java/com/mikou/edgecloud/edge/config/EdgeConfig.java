package com.mikou.edgecloud.edge.config;

import com.mikou.edgecloud.edge.application.IpAllocationServiceImpl;
import com.mikou.edgecloud.edge.domain.service.IpAllocationService;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeNicIpMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Edge 模块配置类
 * 确保关键 Bean 能够被正确注册
 */
@Configuration
public class EdgeConfig {

    @Bean
    public IpAllocationService ipAllocationService(EdgeNicIpMapper ipMapper) {
        return new IpAllocationServiceImpl(ipMapper);
    }
}
