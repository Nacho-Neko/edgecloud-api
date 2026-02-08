package com.mikou.edgecloud.eop.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EopDirection {
    INBOUND(1),
    OUTBOUND(2);

    @EnumValue
    @JsonValue
    private final int id;

    EopDirection(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}