package com.tree.plms.service.impl;

import com.tree.plms.model.entity.Role;
import com.tree.plms.mapper.RoleMapper;
import com.tree.plms.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
