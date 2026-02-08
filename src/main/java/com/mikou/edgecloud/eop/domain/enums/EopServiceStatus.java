package com.mikou.edgecloud.eop.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EopServiceStatus {
    INACTIVE(1),
    OUTBOUND_READY(2),
    ACTIVE(3),
    SUSPENDED(4);

    @EnumValue
    @JsonValue
    private final int id;

    EopServiceStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}