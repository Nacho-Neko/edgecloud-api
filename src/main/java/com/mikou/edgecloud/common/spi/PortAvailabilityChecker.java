package com.mikou.edgecloud.common.spi;

/**
 * 端口可用性查询 SPI
 * EOP 等业务层通过此接口查询端口是否可用，无需直接依赖 Edge 基础设施。
 */
public interface PortAvailabilityChecker {
    boolean isPortAvailable(Integer ipId, Integer port);
}
