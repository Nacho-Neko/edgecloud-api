package com.mikou.edgecloud.account.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountKycPersonEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountKycPersonMapper extends BaseMapper<AccountKycPersonEntity> {
}