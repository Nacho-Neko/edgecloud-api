package com.mikou.edgecloud.account.api;

import com.mikou.edgecloud.account.api.dto.LoginRequest;
import com.mikou.edgecloud.account.api.dto.LoginResponse;
import com.mikou.edgecloud.account.api.dto.RegisterRequest;
import com.mikou.edgecloud.account.application.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AuthService authService;

    public AccountController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public void register(RegisterRequest request) {
        authService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "使用用户名/密码登录 返回 JWT Token.")
    public LoginResponse login(LoginRequest request) {
        return authService.login(request);
    }
}