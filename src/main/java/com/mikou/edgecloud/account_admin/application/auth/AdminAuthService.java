package com.mikou.edgecloud.account_admin.application.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.account.domain.AccountStatus;
import com.mikou.edgecloud.account_admin.domain.RoleStatus;
import com.mikou.edgecloud.account_admin.api.dto.AdminLoginRequest;
import com.mikou.edgecloud.account_admin.api.dto.AdminLoginResponse;
import com.mikou.edgecloud.account_admin.api.dto.AdminValidateResponse;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.entity.AdminAuditEntity;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.entity.AdminEntity;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.entity.AdminPermissionEntity;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.entity.AdminRoleEntity;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.mapper.AdminAuditMapper;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.mapper.AdminMapper;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.mapper.AdminPermissionMapper;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.mapper.AdminRoleMapper;
import com.mikou.edgecloud.common.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminAuthService {

    private final AdminMapper adminMapper;
    private final AdminRoleMapper roleMapper;
    private final AdminPermissionMapper permissionMapper;
    private final AdminAuditMapper auditMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AdminAuthService(
            AdminMapper adminMapper,
            AdminRoleMapper roleMapper,
            AdminPermissionMapper permissionMapper,
            AdminAuditMapper auditMapper,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.adminMapper = adminMapper;
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
        this.auditMapper = auditMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AdminLoginResponse login(AdminLoginRequest req, HttpServletRequest httpRequest) {
        if (req == null || req.getUsernameOrEmail() == null || req.getPassword() == null) {
            throw new IllegalArgumentException("Missing credentials");
        }

        AdminEntity admin = adminMapper.selectOne(new LambdaQueryWrapper<AdminEntity>()
                .eq(AdminEntity::getUsername, req.getUsernameOrEmail())
                .or()
                .eq(AdminEntity::getEmail, req.getUsernameOrEmail())
                .last("limit 1"));

        if (admin == null) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        if (AccountStatus.ENABLED != admin.getAccountStatus()) {
            throw new IllegalArgumentException("Admin account disabled");
        }
        if (!passwordEncoder.matches(req.getPassword(), admin.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // 从数据库加载角色
        AdminRoleEntity role = roleMapper.selectById(admin.getRoleId());
        if (role == null) {
            throw new IllegalArgumentException("Admin role not found");
        }
        if (RoleStatus.ENABLED != role.getStatus()) {
            throw new IllegalArgumentException("Admin role is disabled");
        }

        // 从数据库加载该角色的权限（包括继承自父角色的权限）
        List<AdminPermissionEntity> permissions = permissionMapper.selectByRoleIdWithInheritance(role.getId());
        String permissionCodes = permissions.stream()
                .map(AdminPermissionEntity::getCode)
                .distinct()
                .collect(Collectors.joining(","));

        // 创建带有 admin: 前缀的 Token，用于区分管理员和普通账户
        String authorities = "admin:" + permissionCodes;
        String token = jwtService.createToken(String.valueOf(admin.getId()), authorities);

        // 更新最后登录信息
        String clientIp = getClientIp(httpRequest);
        admin.setLastLoginAt(Instant.now());
        admin.setLastLoginIp(clientIp);
        admin.setUpdatedAt(Instant.now());
        adminMapper.updateById(admin);

        // 记录审计日志
        auditMapper.insert(new AdminAuditEntity()
                .setAdminId(admin.getId())
                .setAction("ADMIN_LOGIN")
                .setIpAddress(clientIp)
                .setUserAgent(httpRequest.getHeader("User-Agent"))
                .setCreatedAt(Instant.now())
        );

        return new AdminLoginResponse()
                .setTokenType("Bearer")
                .setAccessToken(token)
                .setAdminId(admin.getId().toString())
                .setUsername(admin.getUsername())
                .setRole(role.getCode())
                .setRoleDisplayName(role.getName());
    }

    @Transactional
    public void logout(String adminIdStr, HttpServletRequest httpRequest) {
        if (adminIdStr == null || adminIdStr.isBlank()) {
            return;
        }

        try {
            java.util.UUID adminId = java.util.UUID.fromString(adminIdStr);
            
            // 记录审计日志
            auditMapper.insert(new AdminAuditEntity()
                    .setAdminId(adminId)
                    .setAction("ADMIN_LOGOUT")
                    .setIpAddress(getClientIp(httpRequest))
                    .setUserAgent(httpRequest.getHeader("User-Agent"))
                    .setCreatedAt(Instant.now())
            );
        } catch (IllegalArgumentException e) {
            // Invalid UUID, ignore
        }
    }

    @Transactional
    public void changePassword(String username, String newPassword, HttpServletRequest httpRequest) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }

        // 通过用户名查询管理员
        AdminEntity admin = adminMapper.selectOne(new LambdaQueryWrapper<AdminEntity>()
                .eq(AdminEntity::getUsername, username)
                .last("limit 1"));

        if (admin == null) {
            throw new IllegalArgumentException("Admin not found");
        }

        // 直接修改密码（不验证旧密码）
        admin.setPasswordHash(passwordEncoder.encode(newPassword));
        admin.setUpdatedAt(Instant.now());
        adminMapper.updateById(admin);

        // 记录审计日志
        auditMapper.insert(new AdminAuditEntity()
                .setAdminId(admin.getId())
                .setAction("ADMIN_CHANGE_PASSWORD")
                .setIpAddress(getClientIp(httpRequest))
                .setUserAgent(httpRequest.getHeader("User-Agent"))
                .setCreatedAt(Instant.now())
        );
    }

    /**
     * 验证管理员 Token，返回管理员信息
     * 
     * @param adminIdStr 从 Authentication 中获取的管理员 ID
     * @return 管理员验证响应
     */
    public AdminValidateResponse validate(String adminIdStr) {
        if (adminIdStr == null || adminIdStr.isBlank()) {
            throw new IllegalArgumentException("Admin ID is required");
        }

        java.util.UUID adminId;
        try {
            adminId = java.util.UUID.fromString(adminIdStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid admin ID format");
        }

        // 查询管理员信息
        AdminEntity admin = adminMapper.selectById(adminId);
        if (admin == null) {
            throw new IllegalArgumentException("Admin not found");
        }

        // 检查账户状态
        if (AccountStatus.ENABLED != admin.getAccountStatus()) {
            throw new IllegalArgumentException("Admin account is disabled");
        }

        // 查询角色信息
        AdminRoleEntity role = roleMapper.selectById(admin.getRoleId());
        if (role == null) {
            throw new IllegalArgumentException("Admin role not found");
        }

        if (RoleStatus.ENABLED != role.getStatus()) {
            throw new IllegalArgumentException("Admin role is disabled");
        }

        // 返回验证结果
        return new AdminValidateResponse()
                .setAdminId(admin.getId().toString())
                .setUsername(admin.getUsername())
                .setRole(role.getCode())
                .setRoleDisplayName(role.getName());
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 取第一个 IP（如果有多个代理）
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}