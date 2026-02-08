package com.mikou.edgecloud.common.spi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikou.edgecloud.common.config.NatsProperties;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import io.nats.client.Dispatcher;
import io.nats.client.MessageHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Consumer;

@Component
public class NatsClient {
    private static final Logger log = LoggerFactory.getLogger(NatsClient.class);

    private final NatsProperties properties;
    private final ObjectMapper objectMapper;
    private Connection connection;

    public NatsClient(NatsProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        try {
            Options.Builder builder = new Options.Builder()
                    .server(properties.getServer());

            if (properties.getToken() != null && !properties.getToken().isBlank()) {
                builder.token(properties.getToken().toCharArray());
            } else if (properties.getUsername() != null && !properties.getUsername().isBlank()) {
                builder.userInfo(properties.getUsername(), properties.getPassword());
            }

            this.connection = Nats.connect(builder.build());
            log.info("Successfully connected to NATS server at {}", properties.getServer());
        } catch (Exception e) {
            log.error("Failed to connect to NATS server at {}", properties.getServer(), e);
        }
    }

    @PreDestroy
    public void close() {
        if (connection != null) {
            try {
                connection.close();
                log.info("NATS connection closed.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Interrupted while closing NATS connection", e);
            }
        }
    }

    public void publish(String subject, Object message) {
        if (connection == null || connection.getStatus() != Connection.Status.CONNECTED) {
            log.warn("NATS connection is not available. Skipping publish to {}", subject);
            return;
        }

        try {
            byte[] payload = objectMapper.writeValueAsBytes(message);
            connection.publish(subject, payload);
            log.debug("Published message to subject: {}", subject);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message for NATS subject: {}", subject, e);
        } catch (Exception e) {
            log.error("Failed to publish message to NATS subject: {}", subject, e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
