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

    /**
     * 管理员路径安全配置 - 优先级高
     * 测试阶段：已注释鉴权要求，允许所有请求
     */
    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        // 测试阶段：注释掉JWT过滤器
        // JwtAuthenticationFilter adminJwtFilter = new JwtAuthenticationFilter(jwtService, true);

        http
                .securityMatcher("/api/admin/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry -> registry
                        // 测试阶段：允许所有请求，无需鉴权
                        .anyRequest().permitAll()
                        // .requestMatchers("/api/admin/auth/login", "/api/admin/auth/change-password").permitAll()
                        // .requestMatchers("/api/admin/**").authenticated()
                );
                // 测试阶段：注释掉JWT过滤器
                // .addFilterBefore(adminJwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 普通账户路径安全配置
     * 测试阶段：已注释鉴权要求，允许所有请求
     */
    @Bean
    @Order(2)
    public SecurityFilterChain accountSecurityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        // 测试阶段：注释掉JWT过滤器
        // JwtAuthenticationFilter accountJwtFilter = new JwtAuthenticationFilter(jwtService, false);

        http
                .securityMatcher("/api/account/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry -> registry
                        // 测试阶段：允许所有请求，无需鉴权
                        .anyRequest().permitAll()
                        // .requestMatchers("/api/account/login", "/api/account/register").permitAll()
                        // .requestMatchers("/api/account/**").authenticated()
                );
                // 测试阶段：注释掉JWT过滤器
                // .addFilterBefore(accountJwtFilter, UsernamePasswordAuthenticationFilter.class);

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