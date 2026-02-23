package com.mikou.edgecloud.account.domain.service;

import com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountEntity;

import java.util.UUID;

/**
 * Account 领域服务接口
 * 提供给其他领域调用的账户相关服务
 * 
 * 设计原则：
 * 1. 只暴露必要的领域能力，不暴露基础设施细节
 * 2. 为微服务拆分做准备，将来可以改为远程调用
 * 3. 避免跨领域直接调用 Mapper
 */
public interface AccountDomainService {
    
    /**
     * 验证账户是否存在
     * 
     * @param accountId 账户ID
     * @return 账户是否存在
     */
    boolean accountExists(UUID accountId);
    
    /**
     * 获取账户信息
     * 
     * @param accountId 账户ID
     * @return 账户实体，不存在返回 null
     */
    AccountEntity getAccount(UUID accountId);
    
    /**
     * 验证账户存在，不存在则抛出异常
     * 
     * @param accountId 账户ID
     * @throws IllegalArgumentException 账户不存在时抛出
     */
    void validateAccountExists(UUID accountId);
}
