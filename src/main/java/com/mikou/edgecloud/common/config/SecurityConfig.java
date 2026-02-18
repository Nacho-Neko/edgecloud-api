package com.mikou.edgecloud.common.config;

import com.mikou.edgecloud.common.security.JwtAuthenticationFilter;
import com.mikou.edgecloud.common.security.JwtService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    /**
     * 管理员路径安全配置 - 优先级高
     */
    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        JwtAuthenticationFilter adminJwtFilter = new JwtAuthenticationFilter(jwtService, true);

        http
                .securityMatcher("/api/admin/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/api/admin/auth/login").permitAll()
                        .requestMatchers("/api/admin/**").authenticated()
                )
                .addFilterBefore(adminJwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 普通账户路径安全配置
     */
    @Bean
    @Order(2)
    public SecurityFilterChain accountSecurityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        JwtAuthenticationFilter accountJwtFilter = new JwtAuthenticationFilter(jwtService, false);

        http
                .securityMatcher("/api/account/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/api/account/login", "/api/account/register").permitAll()
                        .requestMatchers("/api/account/**").authenticated()
                )
                .addFilterBefore(accountJwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 其他路径默认配置
     */
    @Bean
    @Order(3)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry -> registry
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}