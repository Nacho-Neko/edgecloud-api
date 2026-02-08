package com.mikou.edgecloud.account.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountSensitiveAccessAuditEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountSensitiveAccessAuditMapper extends BaseMapper<AccountSensitiveAccessAuditEntity> {
}