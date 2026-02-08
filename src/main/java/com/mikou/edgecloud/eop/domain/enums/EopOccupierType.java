package com.mikou.edgecloud.eop.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EopOccupierType {
    EOP_APP(1),
    EOP_BOUND(2),
    PROTOCOL(3);

    @EnumValue
    @JsonValue
    private final int id;

    EopOccupierType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
