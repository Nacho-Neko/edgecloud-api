package com.mikou.edgecloud.account.domain.service.impl;

import com.mikou.edgecloud.account.domain.service.AccountDomainService;
import com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountEntity;
import com.mikou.edgecloud.account.infrastructure.persistence.mapper.AccountMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Account 领域服务实现
 */
@Service
public class AccountDomainServiceImpl implements AccountDomainService {
    
    private final AccountMapper accountMapper;
    
    public AccountDomainServiceImpl(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }
    
    @Override
    public boolean accountExists(UUID accountId) {
        if (accountId == null) {
            return false;
        }
        return accountMapper.selectById(accountId) != null;
    }
    
    @Override
    public AccountEntity getAccount(UUID accountId) {
        if (accountId == null) {
            return null;
        }
        return accountMapper.selectById(accountId);
    }
    
    @Override
    public void validateAccountExists(UUID accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        
        AccountEntity account = accountMapper.selectById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountId);
        }
    }
}
