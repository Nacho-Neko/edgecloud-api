package com.mikou.edgecloud.account_admin.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.entity.AdminAuditEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminAuditMapper extends BaseMapper<AdminAuditEntity> {
}
