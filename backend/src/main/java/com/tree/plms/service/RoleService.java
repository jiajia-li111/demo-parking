package com.tree.plms.service;

import com.tree.plms.model.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 存储角色及权限信息 服务类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据角色ID查询角色信息
     * @param roleId 角色标识
     * @return 角色信息
     */
    Role getRoleById(String roleId);

    /**
     * 获取所有角色信息
     * @return 角色信息列表
     */
    List<Role> getAllRoles();

    /**
     * 新增角色信息
     * @param role 角色信息
     * @return 是否新增成功
     */
    boolean addRole(Role role);

    /**
     * 更新角色信息
     * @param role 角色信息
     * @return 是否更新成功
     */
    boolean updateRole(Role role);

    /**
     * 删除角色信息
     * @param roleId 角色标识
     * @return 是否删除成功
     */
    boolean deleteRole(String roleId);

    /**
     * 根据角色名称查询角色信息
     * @param roleName 角色名称
     * @return 角色信息
     */
    Role getRoleByName(String roleName);

    /**
     * 检查角色是否存在
     * @param roleId 角色标识
     * @return 是否存在
     */
    boolean existsByRoleId(String roleId);
}