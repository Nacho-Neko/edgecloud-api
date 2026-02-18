package com.mikou.edgecloud.account_admin.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.entity.AdminRoleEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminRoleMapper extends BaseMapper<AdminRoleEntity> {
}
