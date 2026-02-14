package com.mikou.edgecloud.eop.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EopStatus {
    RUNNING(1, "运行"),
    STOPPED(2, "停止"),
    PAUSED(3, "暂停");

    @EnumValue
    @JsonValue
    private final int id;
    private final String description;

    EopStatus(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
