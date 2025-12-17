package com.tree.plms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tree.plms.enums.ResultCodeEnum;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.SysUser;
import com.tree.plms.model.vo.SysUserVO;
import com.tree.plms.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/users")
@Tag(name = "用户管理", description = "用户管理接口")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    /**
     * 获取用户列表
     */
    @GetMapping
    @Operation(summary = "获取用户列表", description = "获取所有用户的列表")
    public Result<List<SysUser>> getUserList() {
        List<SysUser> users = sysUserService.getAllUsers();
        return Result.success(users);
    }

    /**
     * 创建用户
     */
    @PostMapping
    @Operation(summary = "创建用户", description = "创建一个新的用户")
    public Result<Boolean> createUser(@RequestBody SysUser user) {
        boolean success = sysUserService.addUser(user);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "创建失败");
    }

    /**
     * 更新用户
     */
    @PutMapping("/{userId}")
    @Operation(summary = "更新用户", description = "更新一个用户")
    public Result<Boolean> updateUser(@PathVariable String userId, @RequestBody SysUser user) {
        // 确保路径参数和请求体中的ID一致
        if (!userId.equals(user.getUserId())) {
            return Result.fail(ResultCodeEnum.PARAM_ERROR, "用户ID不匹配");
        }
        boolean success = sysUserService.updateUser(user);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "更新失败");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userId}")
    @Operation(summary = "删除用户", description = "删除一个用户")
    public Result<Boolean> deleteUser(@PathVariable String userId) {
        boolean success = sysUserService.deleteUser(userId);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "删除失败");
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询用户", description = "分页查询用户列表")
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
    @Operation(summary = "修改密码", description = "修改用户密码")
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
    @Operation(summary = "重置密码", description = "重置用户密码")
    public Result<Boolean> resetPassword(
            @PathVariable String userId,
            @RequestParam String newPassword) {
        boolean success = sysUserService.resetPassword(userId, newPassword);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "密码重置失败");
    }
}