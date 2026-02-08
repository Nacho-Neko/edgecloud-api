package com.mikou.edgecloud.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OpenApiConfig {

    public static final String SECURITY_SCHEME_BEARER = "bearerAuth";

    @Bean
    public OpenApiCustomizer uuidExampleCustomizer() {
        return openApi -> {
            Components components = openApi.getComponents();
            if (components != null && components.getSchemas() != null) {
                for (Map.Entry<String, Schema> entry : components.getSchemas().entrySet()) {
                    Schema schema = entry.getValue();
                    if (schema != null && schema.getProperties() != null) {
                        Map<String, Schema> properties = schema.getProperties();
                        for (Schema property : properties.values()) {
                            if ("uuid".equals(property.getFormat())) {
                                property.setExample("00000000-0000-0000-0000-000000000000");
                            }
                        }
                    }
                }
            }
        };
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_BEARER, new SecurityScheme()
                                .name(SECURITY_SCHEME_BEARER)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        ))
                // Make it default for all operations (you can override per-controller later)
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_BEARER));
    }
}