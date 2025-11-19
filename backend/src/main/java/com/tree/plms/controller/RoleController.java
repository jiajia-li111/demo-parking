package com.tree.plms.controller;

import com.tree.plms.enums.ResultCodeEnum;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.Role;
import com.tree.plms.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 获取角色列表
     */
    @GetMapping
    public Result<List<Role>> getRoleList() {
        List<Role> roles = roleService.getAllRoles();
        return Result.success(roles);
    }

    /**
     * 根据ID获取角色信息
     */
    @GetMapping("/{roleId}")
    public Result<Role> getRoleById(@PathVariable String roleId) {
        Role role = roleService.getRoleById(roleId);
        if (role == null) {
            return Result.fail(ResultCodeEnum.NOT_FOUND, "角色不存在");
        }
        return Result.success(role);
    }

    /**
     * 新增角色
     */
    @PostMapping
    public Result<Boolean> addRole(@RequestBody Role role) {
        boolean success = roleService.addRole(role);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "新增失败");
    }

    /**
     * 更新角色信息
     */
    @PutMapping
    public Result<Boolean> updateRole(@RequestBody Role role) {
        boolean success = roleService.updateRole(role);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "更新失败");
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{roleId}")
    public Result<Boolean> deleteRole(@PathVariable String roleId) {
        boolean success = roleService.deleteRole(roleId);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "删除失败");
    }
}