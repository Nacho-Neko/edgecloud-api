package com.mikou.edgecloud.edge.domain.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Edge 节点 NATS 通知消息（传输层、协议层等 Edge 域事件）
 */
public class EdgeNotifyMessage {

    public static final String ACTION_CREATE_TRANSFER = "CREATE_TRANSFER";
    public static final String ACTION_DELETE_TRANSFER = "DELETE_TRANSFER";
    public static final String ACTION_CREATE_PROTOCOL = "CREATE_PROTOCOL";
    public static final String ACTION_DELETE_PROTOCOL = "DELETE_PROTOCOL";

    private String action;
    private UUID edgeTag;
    private Object data;
    private Instant timestamp;

    public EdgeNotifyMessage() {
        this.timestamp = Instant.now();
    }

    public EdgeNotifyMessage(String action, UUID edgeTag, Object data) {
        this();
        this.action = action;
        this.edgeTag = edgeTag;
        this.data = data;
    }

    public String getAction() { return action; }
    public EdgeNotifyMessage setAction(String action) { this.action = action; return this; }

    public UUID getEdgeTag() { return edgeTag; }
    public EdgeNotifyMessage setEdgeTag(UUID edgeTag) { this.edgeTag = edgeTag; return this; }

    public Object getData() { return data; }
    public EdgeNotifyMessage setData(Object data) { this.data = data; return this; }

    public Instant getTimestamp() { return timestamp; }
    public EdgeNotifyMessage setTimestamp(Instant timestamp) { this.timestamp = timestamp; return this; }
}
