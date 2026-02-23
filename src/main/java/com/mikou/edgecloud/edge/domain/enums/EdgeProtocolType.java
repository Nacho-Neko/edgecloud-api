package com.mikou.edgecloud.edge.domain.enums;

/**
 * Edge 传输协议类型
 * 定义每种协议支持的端口模式。
 */
public enum EdgeProtocolType {

    /**
     * QUIC 协议 — 基于 UDP，仅支持单端口绑定。
     */
    QUIC(PortMode.SINGLE_PORT);

    public enum PortMode {
        /** 仅允许指定单个端口 */
        SINGLE_PORT,
        /** 仅允许指定端口范围（格式 "start-end"） */
        PORT_RANGE
    }

    private final PortMode portMode;

    EdgeProtocolType(PortMode portMode) {
        this.portMode = portMode;
    }

    public PortMode getPortMode() {
        return portMode;
    }

    public boolean requiresSinglePort() {
        return portMode == PortMode.SINGLE_PORT;
    }

    public boolean requiresPortRange() {
        return portMode == PortMode.PORT_RANGE;
    }
}
