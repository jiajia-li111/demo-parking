package com.tree.plms.service.impl;

import com.tree.plms.enums.ResultCodeEnum;
import com.tree.plms.enums.UserStatusEnum;
import com.tree.plms.exception.BusinessException;
import com.tree.plms.mapper.RoleMapper;
import com.tree.plms.model.dto.request.LoginReq;
import com.tree.plms.model.entity.Role;
import com.tree.plms.model.entity.SysUser;
import com.tree.plms.mapper.SysUserMapper;
import com.tree.plms.model.vo.LoginVO;
import com.tree.plms.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.plms.util.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 存储操作人员信息 服务实现类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PasswordEncoder passwordEncoder; // Spring Security加密器

    @Resource
    private JwtUtil jwtUtil; // JWT工具类
    @Override
    public LoginVO login(LoginReq req){
        SysUser user = sysUserMapper.selectByUsername(req.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED, "用户名或密码错误");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED, "用户名或密码错误");
        }

        if (UserStatusEnum.DISABLED.getCode().equals(user.getStatus())) {
            throw new BusinessException(ResultCodeEnum.FORBIDDEN, "账号已禁用");
        }

        Role role = roleMapper.selectById(user.getRoleId());
        if (role == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "角色不存在");
        }

        String token = jwtUtil.generateToken(user.getUserId());

        LoginVO vo = new LoginVO();
        vo.setUserId(user.getUserId());
        vo.setUsername(user.getUsername());
        vo.setRoleName(role.getRoleName());
        vo.setPermissions(role.getPermissions().split(","));
        vo.setToken(token);
        return vo;
    }
}

