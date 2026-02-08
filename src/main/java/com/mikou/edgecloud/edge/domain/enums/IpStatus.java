package com.mikou.edgecloud.edge.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IpStatus {
    ACTIVE(1),
    REMOVED(0);

    @EnumValue
    @JsonValue
    private final int value;

    IpStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}