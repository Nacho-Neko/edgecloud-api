package com.mikou.edgecloud.eop.domain.events;

import java.time.Instant;
import java.util.UUID;

public class EopNotifyMessage {
    private String action;
    private UUID edgeTag;
    private Object data;
    private Instant timestamp;

    public EopNotifyMessage() {
        this.timestamp = Instant.now();
    }

    public EopNotifyMessage(String action, UUID edgeTag, Object data) {
        this();
        this.action = action;
        this.edgeTag = edgeTag;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public UUID getEdgeTag() {
        return edgeTag;
    }

    public void setEdgeTag(UUID edgeTag) {
        this.edgeTag = edgeTag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public static final String ACTION_CREATE_TRANSFER = "CREATE_TRANSFER";
    public static final String ACTION_DELETE_TRANSFER = "DELETE_TRANSFER";
    public static final String ACTION_CREATE_BOUND = "CREATE_BOUND";
    public static final String ACTION_DELETE_BOUND = "DELETE_BOUND";
    public static final String ACTION_UPDATE_SETTINGS = "UPDATE_SETTINGS";
}
