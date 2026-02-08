package com.mikou.edgecloud.eop.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EopSettings {
    private HashMap<String, ProtocolListener> listeners;
    private String allowedPortRange; // 允许的端口范围，例如 "1024:65535"

    public EopSettings() {
        listeners = new HashMap<>();
    }

    public Map<String, ProtocolListener> getListeners() {
        return listeners;
    }

    public EopSettings setListeners(Map<String, ProtocolListener> listeners) {
        this.listeners = new HashMap<>(listeners);
        return this;
    }

    public ProtocolListener getListener(String protocol) {
        return listeners.get(protocol);
    }

    public EopSettings setListener(String protocol, ProtocolListener listener) {
        this.listeners.put(protocol, listener);
        return this;
    }

    public String getAllowedPortRange() {
        return allowedPortRange;
    }

    public EopSettings setAllowedPortRange(String allowedPortRange) {
        this.allowedPortRange = allowedPortRange;
        return this;
    }

    public EopSettings addListener(String protocol, ProtocolListener listener) {
        if (this.listeners == null) {
            this.listeners = new HashMap<>();
        }
        if (protocol != null) {
            this.listeners.put(protocol, listener);
        }
        return this;
    }

    public boolean hasListeners() {
        return listeners != null && !listeners.isEmpty();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProtocolListener {
        private Integer listenIp; // 改为传输 EdgeNicIP 的主键
        private Integer port;
        private PortRange portRange;

        public ProtocolListener() {
        }

        public Integer getListenIp() {
            return listenIp;
        }

        public ProtocolListener setListenIp(Integer listenIp) {
            this.listenIp = listenIp;
            return this;
        }

        public Integer getPort() {
            return port;
        }

        public ProtocolListener setPort(Integer port) {
            this.port = port;
            return this;
        }

        public PortRange getPortRange() {
            return portRange;
        }

        public ProtocolListener setPortRange(PortRange portRange) {
            this.portRange = portRange;
            return this;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PortRange {
        private Integer start;
        private Integer end;

        public PortRange() {
        }

        public PortRange(Integer start, Integer end) {
            this.start = start;
            this.end = end;
        }

        public Integer getStart() {
            return start;
        }

        public PortRange setStart(Integer start) {
            this.start = start;
            return this;
        }

        public Integer getEnd() {
            return end;
        }

        public PortRange setEnd(Integer end) {
            this.end = end;
            return this;
        }

        public boolean isValid() {
            return start != null && end != null && start <= end && start > 0 && end <= 65535;
        }
    }
}