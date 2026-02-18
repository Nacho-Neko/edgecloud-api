package com.mikou.edgecloud.account.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.account.api.dto.AccountWithKycDto;
import com.mikou.edgecloud.account.application.admin.AccountAdminService;
import com.mikou.edgecloud.business.eop.api.dto.EopServiceDto;
import com.mikou.edgecloud.business.domain.BusinessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/account/admin")
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

    @GetMapping("/business")
    @Operation(summary = "查询业务列表", description = "查询 Business 服务列表，支持按账户ID、业务类型和状态筛选")
    public Page<EopServiceDto> listBusiness(
            @Parameter(description = "账户ID（可选）") 
            @RequestParam(required = false) UUID accountId,
            @Parameter(description = "业务类型（可选），如：EOP") 
            @RequestParam(required = false) String businessType,
            @Parameter(description = "服务状态（可选）：ACTIVE, SUSPENDED, EXPIRED")
            @RequestParam(required = false) BusinessStatus status,
            @Parameter(description = "页码，从0开始") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") 
            @RequestParam(defaultValue = "10") int size) {
        return accountAdminService.listBusinessServices(accountId, businessType, status, page, size);
    }
}
