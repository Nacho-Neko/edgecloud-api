package com.mikou.edgecloud.account_admin.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mikou.edgecloud.account_admin.infrastructure.persistence.entity.AdminPermissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.UUID;

@Mapper
public interface AdminPermissionMapper extends BaseMapper<AdminPermissionEntity> {
    
    /**
     * 根据角色ID查询权限列表（不含继承）
     */
    @Select("SELECT p.* FROM admin_permission p " +
            "INNER JOIN admin_role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    List<AdminPermissionEntity> selectByRoleId(UUID roleId);

    /**
     * 递归查询角色及其所有父角色的权限（支持角色继承）
     * 使用 PostgreSQL 的 WITH RECURSIVE 递归查询
     */
    @Select("WITH RECURSIVE role_hierarchy AS ( " +
            "    -- 基础查询：当前角色 " +
            "    SELECT id, parent_id FROM admin_role WHERE id = #{roleId} " +
            "    UNION ALL " +
            "    -- 递归查询：父角色 " +
            "    SELECT r.id, r.parent_id " +
            "    FROM admin_role r " +
            "    INNER JOIN role_hierarchy rh ON r.id = rh.parent_id " +
            ") " +
            "SELECT DISTINCT p.* FROM admin_permission p " +
            "INNER JOIN admin_role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id IN (SELECT id FROM role_hierarchy)")
    List<AdminPermissionEntity> selectByRoleIdWithInheritance(UUID roleId);
}
