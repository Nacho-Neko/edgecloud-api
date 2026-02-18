package com.mikou.edgecloud.account.application.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.account.api.dto.AccountWithKycDto;
import com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountEntity;
import com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountKycEntity;
import com.mikou.edgecloud.account.infrastructure.persistence.mapper.AccountKycMapper;
import com.mikou.edgecloud.account.infrastructure.persistence.mapper.AccountMapper;
import com.mikou.edgecloud.business.eop.api.dto.EopServiceDto;
import com.mikou.edgecloud.business.eop.application.EopService;
import com.mikou.edgecloud.business.domain.BusinessStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountAdminService {

    private final AccountMapper accountMapper;
    private final AccountKycMapper accountKycMapper;
    private final EopService eopService;

    public AccountAdminService(AccountMapper accountMapper, 
                              AccountKycMapper accountKycMapper,
                              EopService eopService) {
        this.accountMapper = accountMapper;
        this.accountKycMapper = accountKycMapper;
        this.eopService = eopService;
    }

    /**
     * 获取账户列表（包含KYC信息）
     */
    public List<AccountWithKycDto> listAccountsWithKyc() {
        // 查询所有账户
        List<AccountEntity> accounts = accountMapper.selectList(null);
        
        if (accounts.isEmpty()) {
            return List.of();
        }

        // 提取账户ID列表
        List<UUID> accountIds = accounts.stream()
                .map(AccountEntity::getId)
                .collect(Collectors.toList());

        // 查询所有账户的KYC信息
        LambdaQueryWrapper<AccountKycEntity> kycQuery = new LambdaQueryWrapper<>();
        kycQuery.in(AccountKycEntity::getAccountId, accountIds);
        List<AccountKycEntity> kycList = accountKycMapper.selectList(kycQuery);

        // 构建账户ID到KYC的映射（每个账户取最新的一条KYC记录）
        Map<UUID, AccountKycEntity> accountKycMap = kycList.stream()
                .collect(Collectors.toMap(
                        AccountKycEntity::getAccountId,
                        kyc -> kyc,
                        (existing, replacement) -> {
                            // 保留时间较新的记录
                            if (replacement.getCreatedAt().isAfter(existing.getCreatedAt())) {
                                return replacement;
                            }
                            return existing;
                        }
                ));

        // 组装结果
        return accounts.stream()
                .map(account -> {
                    AccountWithKycDto dto = new AccountWithKycDto()
                            .setId(account.getId())
                            .setUsername(account.getUsername())
                            .setEmail(account.getEmail())
                            .setStatus(account.getStatus())
                            .setCreatedAt(account.getCreatedAt())
                            .setUpdatedAt(account.getUpdatedAt());

                    // 填充KYC信息（如果存在）
                    AccountKycEntity kyc = accountKycMap.get(account.getId());
                    if (kyc != null) {
                        dto.setKycId(kyc.getId())
                                .setKycProvider(kyc.getProvider())
                                .setKycType(kyc.getKycType())
                                .setKycStatus(kyc.getStatus())
                                .setKycSubmittedAt(kyc.getSubmittedAt())
                                .setKycVerifiedAt(kyc.getVerifiedAt());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询 Business 服务列表 - 支持筛选条件
     * 通过各业务应用服务层完成调用
     * 
     * @param accountId 账户ID（可选）
     * @param businessType 业务类型（可选），如 "EOP"
     * @param status 服务状态（可选）
     * @param page 页码
     * @param size 每页大小
     * @return 分页的服务列表
     */
    public Page<EopServiceDto> listBusinessServices(UUID accountId, String businessType, 
                                                    BusinessStatus status, int page, int size) {
        // 如果指定了 accountId，验证账户是否存在
        if (accountId != null) {
            AccountEntity account = accountMapper.selectById(accountId);
            if (account == null) {
                throw new IllegalArgumentException("Account not found: " + accountId);
            }
        }

        // 根据业务类型动态调用对应的服务层
        // 目前只实现了 EOP，其他业务类型可以后续扩展
        if (businessType == null || "EOP".equalsIgnoreCase(businessType)) {
            // 通过 EOP 应用服务层查询服务列表
            Pageable pageable = PageRequest.of(page, size);
            return eopService.listServicesWithFilter(accountId, status, pageable);
        } else {
            // 其他业务类型暂未实现，返回空结果
            Page<EopServiceDto> emptyPage = new Page<>(page + 1, size, 0);
            emptyPage.setRecords(List.of());
            return emptyPage;
        }
    }
}
