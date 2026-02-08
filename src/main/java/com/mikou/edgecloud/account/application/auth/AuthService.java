package com.mikou.edgecloud.account.application.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.account.api.dto.LoginRequest;
import com.mikou.edgecloud.account.api.dto.LoginResponse;
import com.mikou.edgecloud.account.api.dto.RegisterRequest;
import com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountEntity;
import com.mikou.edgecloud.account.infrastructure.persistence.mapper.AccountMapper;
import com.mikou.edgecloud.common.security.JwtService;
import java.time.Instant;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AccountMapper accountMapper, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public void register(RegisterRequest req) {
        if (req == null || req.getUsername() == null || req.getPassword() == null) {
            throw new IllegalArgumentException("Missing username/password");
        }

        boolean usernameExists = accountMapper.exists(new LambdaQueryWrapper<AccountEntity>()
                .eq(AccountEntity::getUsername, req.getUsername()));
        if (usernameExists) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (req.getEmail() != null && !req.getEmail().isBlank()) {
            boolean emailExists = accountMapper.exists(new LambdaQueryWrapper<AccountEntity>()
                    .eq(AccountEntity::getEmail, req.getEmail()));
            if (emailExists) {
                throw new IllegalArgumentException("Email already exists");
            }
        }

        Instant now = Instant.now();
        accountMapper.insert(new AccountEntity()
                .setUsername(req.getUsername())
                .setEmail(req.getEmail())
                .setPasswordHash(passwordEncoder.encode(req.getPassword()))
                .setStatus("ACTIVE")
                .setCreatedAt(now)
                .setUpdatedAt(now)
        );
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest req) {
        if (req == null || req.getUsernameOrEmail() == null || req.getPassword() == null) {
            throw new IllegalArgumentException("Missing credentials");
        }

        AccountEntity account = accountMapper.selectOne(new LambdaQueryWrapper<AccountEntity>()
                .eq(AccountEntity::getUsername, req.getUsernameOrEmail())
                .or()
                .eq(AccountEntity::getEmail, req.getUsernameOrEmail())
                .last("limit 1"));

        if (account == null) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        if (!"ACTIVE".equals(account.getStatus())) {
            throw new IllegalArgumentException("Account disabled");
        }
        if (!passwordEncoder.matches(req.getPassword(), account.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Minimal: hardcode authorities for now; replace with DB roles later.
        String authorities = "KYC_VIEW,KYC_REVEAL";
        String token = jwtService.createToken(String.valueOf(account.getId()), authorities);

        return new LoginResponse()
                .setTokenType("Bearer")
                .setAccessToken(token);
    }
}