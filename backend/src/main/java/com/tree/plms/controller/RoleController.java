package com.tree.plms.controller;

import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.Role;
import com.tree.plms.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/roles")
@Tag(name = "角色管理")
public class RoleController {
    
    @Resource
    private RoleService roleService;
    
    /**
     * 获取角色列表
     */
    @GetMapping
    @Operation(summary = "获取角色列表", description = "获取所有角色的列表")
    public Result<List<Role>> getRoleList() {
        List<Role> roles = roleService.getAllRoles();
        return Result.success(roles);
    }
}