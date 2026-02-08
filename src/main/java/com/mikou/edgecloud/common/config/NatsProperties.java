package com.mikou.edgecloud.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "nats")
public class NatsProperties {
    private String server;
    private String username;
    private String password;
    private String token;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
