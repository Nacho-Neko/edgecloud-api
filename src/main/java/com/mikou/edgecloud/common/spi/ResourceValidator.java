package com.mikou.edgecloud.common.spi;

import java.util.UUID;

/**
 * 资源验证器 SPI
 * 各领域实现此接口，提供资源存在性验证
 */
public interface ResourceValidator {
    /**
     * 验证资源是否存在
     */
    boolean exists(UUID resourceTag);

    /**
     * 支持的资源类型
     */
    String getSupportedType();
}