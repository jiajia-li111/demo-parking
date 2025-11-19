package com.tree.plms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tree.plms.enums.ResultCodeEnum;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.SysUser;
import com.tree.plms.model.vo.SysUserVO;
import com.tree.plms.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 存储操作人员信息 前端控制器
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@RestController
@RequestMapping("/api/users")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    /**
     * 获取用户列表
     */
    @GetMapping
    public Result<List<SysUser>> getUserList() {
        List<SysUser> users = sysUserService.getAllUsers();
        return Result.success(users);
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{userId}")
    public Result<SysUser> getUserById(@PathVariable String userId) {
        SysUser user = sysUserService.getUserById(userId);
        if (user == null) {
            return Result.fail(ResultCodeEnum.NOT_FOUND, "用户不存在");
        }
        return Result.success(user);
    }

    /**
     * 新增用户
     */
    @PostMapping
    public Result<Boolean> addUser(@RequestBody SysUser user) {
        boolean success = sysUserService.addUser(user);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "新增失败");
    }

    /**
     * 更新用户信息
     */
    @PutMapping
    public Result<Boolean> updateUser(@RequestBody SysUser user) {
        boolean success = sysUserService.updateUser(user);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "更新失败");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userId}")
    public Result<Boolean> deleteUser(@PathVariable String userId) {
        boolean success = sysUserService.deleteUser(userId);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "删除失败");
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    public Result<IPage<SysUserVO>> getUserPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String roleId,
            @RequestParam(required = false) String status) {
        Page<SysUserVO> page = new Page<>(current, size);
        IPage<SysUserVO> userPage = sysUserService.getUserPage(page, username, roleId, status);
        return Result.success(userPage);
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Boolean> updatePassword(
            @RequestParam String userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        boolean success = sysUserService.updatePassword(userId, oldPassword, newPassword);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "密码修改失败");
    }

    /**
     * 重置密码
     */
    @PutMapping("/{userId}/reset-password")
    public Result<Boolean> resetPassword(
            @PathVariable String userId,
            @RequestParam String newPassword) {
        boolean success = sysUserService.resetPassword(userId, newPassword);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "密码重置失败");
    }
}
