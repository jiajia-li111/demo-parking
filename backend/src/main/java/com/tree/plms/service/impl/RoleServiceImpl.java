package com.tree.plms.service.impl;

import com.tree.plms.model.entity.Role;
import com.tree.plms.mapper.RoleMapper;
import com.tree.plms.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * <p>
 * 存储角色及权限信息 服务实现类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public Role getRoleById(String roleId) {
        return baseMapper.selectById(roleId);
    }

    @Override
    public List<Role> getAllRoles() {
        return baseMapper.selectList(null);
    }

    @Override
    public boolean addRole(Role role) {
        return save(role);
    }

    @Override
    public boolean updateRole(Role role) {
        return updateById(role);
    }

    @Override
    public boolean deleteRole(String roleId) {
        return removeById(roleId);
    }

    @Override
    public Role getRoleByName(String roleName) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name", roleName);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean existsByRoleId(String roleId) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        return baseMapper.exists(queryWrapper);
    }
}