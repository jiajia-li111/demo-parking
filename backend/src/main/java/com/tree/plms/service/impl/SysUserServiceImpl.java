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
import com.tree.plms.model.vo.SysUserVO;
import com.tree.plms.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.plms.util.JwtUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.plms.model.dto.response.Result;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public LoginVO login(LoginReq req) {
        SysUser user = sysUserMapper.selectByUsername(req.getUsername());

        if (user == null) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED, "用户名或密码错误");
        }

        // 验证密码
        System.out.println(user.getPassword());
        System.out.println(req.getPassword());
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
    
    @Override
    public Result<String> logout(String token) {
        // 验证token是否有效
        if (!jwtUtil.validateToken(token)) {
            return Result.fail(ResultCodeEnum.UNAUTHORIZED, "无效的令牌");
        }
        
        // 在实际项目中，这里可能需要将token加入黑名单或缓存
        // 由于当前没有实现token黑名单机制，仅返回成功
        
        return Result.success("登出成功");
    }

    @Override
    public SysUser getUserById(String userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<SysUser> getAllUsers() {
        return baseMapper.selectList(null);
    }

    @Override
    public boolean addUser(SysUser user) {
        // 检查用户名是否已存在
        if (getUserByUsername(user.getUsername()) != null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR, "用户名已存在");
        }

        if (user.getUserId() == null) {
            // 查询当前最大的用户ID
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("user_id");
            queryWrapper.last("LIMIT 1");
            SysUser lastUser = baseMapper.selectOne(queryWrapper);

            String newUserId;
            if (lastUser != null) {
                // 提取数字部分并加1
                String lastId = lastUser.getUserId();
                int num = Integer.parseInt(lastId.substring(1)) + 1;
                newUserId = String.format("u%05d", num);
            } else {
                // 第一个用户
                newUserId = "u00001";
            }
            user.setUserId(newUserId);
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 定义允许的部门范围
        Set<String> allowedDepartments = new HashSet<>(Arrays.asList("管理中心", "测试中心", "用户中心"));

        // 检查部门是否在允许范围内
        System.out.println(user.getDepartment());
        if (user.getDepartment() != null) {
            if (!allowedDepartments.contains(user.getDepartment())) {
                throw new BusinessException(ResultCodeEnum.PARAM_ERROR, "部门不在允许范围内");
            }
        } else {
            // 设置默认部门
            user.setDepartment("管理中心");
        }

        // 检查状态是否在允许范围内
        if (user.getStatus() != null) {
            if (!UserStatusEnum.isValidCode(user.getStatus())) {
                throw new BusinessException(ResultCodeEnum.PARAM_ERROR, "状态值不在允许范围内");
            }
        } else {
            // 设置默认状态为启用
            user.setStatus(UserStatusEnum.ENABLED.getCode());
        }
        
        return save(user);
    }

    @Override
    public boolean updateUser(SysUser user) {
        // 不更新密码字段，密码需要单独更新
        SysUser existingUser = getUserById(user.getUserId());
        if (existingUser == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "用户不存在");
        }

        // 检查状态是否在允许范围内
        if (user.getStatus() != null && !UserStatusEnum.isValidCode(user.getStatus())) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR, "状态值不在允许范围内");
        }

        // 保留原密码
        user.setPassword(existingUser.getPassword());
        
        return updateById(user);
    }

    @Override
    public boolean deleteUser(String userId) {
        return removeById(userId);
    }

    @Override
    public List<SysUser> getUsersByRoleId(String roleId) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysUser> getUsersByStatus(String status) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<SysUserVO> getUserPage(Page<SysUserVO> page, String username, String roleId, String status) {
        return sysUserMapper.selectUserPage(page, username, roleId, status);
    }

    @Override
    public boolean updatePassword(String userId, String oldPassword, String newPassword) {
        SysUser user = getUserById(userId);
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "用户不存在");
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR, "旧密码错误");
        }
        
        // 更新新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        return updateById(user);
    }

    @Override
    public boolean resetPassword(String userId, String newPassword) {
        SysUser user = getUserById(userId);
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "用户不存在");
        }
        
        // 直接设置新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        return updateById(user);
    }

    @Override
    public SysUserVO getCurrentUserByToken(String token) {
        // 验证token是否有效
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED, "无效的令牌");
        }
        
        // 从token中获取用户ID
        String userId = jwtUtil.getUserIdFromToken(token);
        
        // 查询用户信息
        SysUser user = getUserById(userId);
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "用户不存在");
        }
        
        // 查询角色信息
        Role role = roleMapper.selectById(user.getRoleId());
        if (role == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "角色不存在");
        }
        
        // 构建用户VO
        SysUserVO userVO = new SysUserVO();
        userVO.setUserId(user.getUserId());
        userVO.setUsername(user.getUsername());
        userVO.setRoleId(user.getRoleId());
        userVO.setRoleName(role.getRoleName());
        userVO.setDepartment(user.getDepartment());
        userVO.setStatus(user.getStatus());
        userVO.setStatusDesc(UserStatusEnum.getDescByCode(user.getStatus()));
        
        return userVO;
    }
}