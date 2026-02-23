package com.mikou.edgecloud.account_admin.api;

import com.mikou.edgecloud.account_admin.api.dto.AdminChangePasswordRequest;
import com.mikou.edgecloud.account_admin.api.dto.AdminLoginRequest;
import com.mikou.edgecloud.account_admin.api.dto.AdminLoginResponse;
import com.mikou.edgecloud.account_admin.api.dto.AdminValidateResponse;
import com.mikou.edgecloud.account_admin.application.auth.AdminAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@Tag(name = "Admin Auth", description = "管理员认证接口")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @PostMapping("/login")
    @Operation(summary = "管理员登录", description = "使用用户名/邮箱和密码登录后台管理系统，返回管理员专用 JWT Token")
    public AdminLoginResponse login(@RequestBody AdminLoginRequest request, HttpServletRequest httpRequest) {
        return adminAuthService.login(request, httpRequest);
    }

    @PostMapping("/logout")
    @Operation(summary = "管理员登出", description = "记录管理员登出操作审计日志")
    public void logout(Authentication authentication, HttpServletRequest httpRequest) {
        String adminId = (authentication != null ? authentication.getName() : null);
        adminAuthService.logout(adminId, httpRequest);
    }

    @PostMapping("/change-password")
    @Operation(summary = "修改密码", description = "修改管理员密码，不需要登录验证，直接传递用户名和新密码即可")
    public void changePassword(
            @RequestBody AdminChangePasswordRequest request,
            HttpServletRequest httpRequest
    ) {
        adminAuthService.changePassword(request.getUsername(), request.getNewPassword(), httpRequest);
    }

    @GetMapping("/validate")
    @Operation(summary = "验证管理员 Token", description = "验证当前管理员 Token 是否有效，并返回管理员基本信息")
    public AdminValidateResponse validate(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalArgumentException("Not authenticated");
        }
        return adminAuthService.validate(authentication.getName());
    }
}