package com.mikou.edgecloud.edge.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * IP 端口占用者类型
 * 归属 Edge 层，因为端口占用是节点级资源，任何业务都可能成为占用者。
 */
public enum IpPortOccupierType {

    EOP_APP(1),      // EOP 业务：EopApp 入站监听
    EOP_BOUND(2),    // EOP 业务：EopBound 端口
    PROTOCOL(3),     // 系统协议保留端口（如 SSH/22、HTTP/80 等）
    EDGE_SYSTEM(4),  // Edge 传输层：节点间隧道端点、CDN 回源出站绑定等
    CDN_SERVICE(5);  // CDN 业务：用户入站监听（预留）

    @EnumValue
    @JsonValue
    private final int id;

    IpPortOccupierType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
