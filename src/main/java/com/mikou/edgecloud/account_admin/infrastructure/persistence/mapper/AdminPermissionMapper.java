package com.mikou.edgecloud.account_admin.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.entity.AdminPermissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.UUID;

@Mapper
public interface AdminPermissionMapper extends BaseMapper<AdminPermissionEntity> {
    
    /**
     * 根据角色ID查询权限列表（不含继承）
     * SQL 定义在 mapper/account_admin/AdminPermissionMapper.xml
     */
    List<AdminPermissionEntity> selectByRoleId(@Param("roleId") UUID roleId);

    /**
     * 递归查询角色及其所有父角色的权限（支持角色继承）
     * 使用 PostgreSQL 的 WITH RECURSIVE 递归查询
     * SQL 定义在 mapper/account_admin/AdminPermissionMapper.xml
     */
    List<AdminPermissionEntity> selectByRoleIdWithInheritance(@Param("roleId") UUID roleId);
}
