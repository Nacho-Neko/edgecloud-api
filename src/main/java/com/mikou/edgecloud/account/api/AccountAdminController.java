package com.mikou.edgecloud.account.api;

import com.mikou.edgecloud.account.api.dto.AccountWithKycDto;
import com.mikou.edgecloud.account.application.admin.AccountAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/account")
@Tag(name = "账户管理", description = "账户管理相关接口")
public class AccountAdminController {

    private final AccountAdminService accountAdminService;

    public AccountAdminController(AccountAdminService accountAdminService) {
        this.accountAdminService = accountAdminService;
    }

    @GetMapping("/list")
    @Operation(summary = "获取账户列表", description = "获取所有账户列表，包含KYC信息")
    public List<AccountWithKycDto> listAccounts() {
        return accountAdminService.listAccountsWithKyc();
    }

}
