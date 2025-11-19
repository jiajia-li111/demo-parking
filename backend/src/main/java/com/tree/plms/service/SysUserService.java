package com.tree.plms.service;

import com.tree.plms.model.dto.request.LoginReq;
import com.tree.plms.model.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.plms.model.vo.LoginVO;
import com.tree.plms.model.vo.SysUserVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tree.plms.model.dto.response.Result;

import java.util.List;

/**
 * <p>
 * 存储操作人员信息 服务类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户登录
     * @param req 登录请求参数
     * @return 登录响应结果
     */
    LoginVO login(LoginReq req);

    /**
     * 用户登出
     * @param token JWT令牌
     * @return 登出结果
     */
    Result<String> logout(String token);

    /**
     * 根据用户ID查询用户信息
     * @param userId 用户唯一标识
     * @return 用户信息
     */
    SysUser getUserById(String userId);

    /**
     * 根据用户名查询用户信息
     * @param username 登录用户名
     * @return 用户信息
     */
    SysUser getUserByUsername(String username);

    /**
     * 获取所有用户信息
     * @return 用户信息列表
     */
    List<SysUser> getAllUsers();

    /**
     * 新增用户信息
     * @param user 用户信息
     * @return 是否新增成功
     */
    boolean addUser(SysUser user);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 是否更新成功
     */
    boolean updateUser(SysUser user);

    /**
     * 删除用户信息
     * @param userId 用户唯一标识
     * @return 是否删除成功
     */
    boolean deleteUser(String userId);

    /**
     * 根据角色ID查询用户列表
     * @param roleId 关联角色ID
     * @return 用户信息列表
     */
    List<SysUser> getUsersByRoleId(String roleId);

    /**
     * 根据用户状态查询用户列表
     * @param status 状态（01=启用，02=禁用）
     * @return 用户信息列表
     */
    List<SysUser> getUsersByStatus(String status);

    /**
     * 分页查询用户信息
     * @param page 分页参数
     * @param username 用户名（可选）
     * @param roleId 角色ID（可选）
     * @param status 状态（可选）
     * @return 分页查询结果
     */
    IPage<SysUserVO> getUserPage(Page<SysUserVO> page, String username, String roleId, String status);

    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean updatePassword(String userId, String oldPassword, String newPassword);

    /**
     * 重置用户密码
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 是否重置成功
     */
    boolean resetPassword(String userId, String newPassword);
}