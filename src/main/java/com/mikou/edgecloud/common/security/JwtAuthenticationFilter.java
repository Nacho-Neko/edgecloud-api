package com.mikou.edgecloud.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final boolean requireAdmin;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this(jwtService, false);
    }

    public JwtAuthenticationFilter(JwtService jwtService, boolean requireAdmin) {
        this.jwtService = jwtService;
        this.requireAdmin = requireAdmin;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring("Bearer ".length()).trim();
        try {
            Jws<Claims> jws = jwtService.parseAndValidate(token);
            Claims claims = jws.getPayload();

            String subject = claims.getSubject();
            String auth = claims.get("auth", String.class);

            // 验证 Token 类型与路径匹配
            if (requireAdmin && !JwtTokenType.isAdminToken(jws)) {
                // 管理员路径但使用了普通账户 Token
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            } else if (!requireAdmin && JwtTokenType.isAdminToken(jws)) {
                // 普通账户路径但使用了管理员 Token
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }

            var authorities = Arrays.stream((auth == null ? "" : auth).split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            var authentication = new UsernamePasswordAuthenticationToken(subject, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (RuntimeException ex) {
            // Invalid token -> treat as unauthenticated
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}