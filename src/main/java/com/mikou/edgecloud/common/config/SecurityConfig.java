package com.mikou.edgecloud.common.config;

import com.mikou.edgecloud.common.security.JwtAuthenticationFilter;
import com.mikou.edgecloud.common.security.JwtService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class SecurityConfig {

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.ttl-seconds:3600}")
    private long jwtTtlSeconds;

    @Bean
    public JwtService jwtService() {
        return new JwtService(jwtSecret, jwtTtlSeconds);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtService);

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry -> registry
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}