package com.tree.plms.controller;

import com.tree.plms.model.dto.request.LoginReq;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.vo.LoginVO;
import com.tree.plms.model.vo.SysUserVO;
import com.tree.plms.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器（处理登录登出）
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "认证接口", description = "处理用户登录、登出等操作")
public class AuthController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户输入用户名和密码登录系统，返回登录凭证")
    public Result<LoginVO> login(@RequestBody LoginReq req) {
        LoginVO loginVO = sysUserService.login(req);
        return Result.success(loginVO);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出系统，删除登录凭证")
    public Result<String> logout(HttpServletRequest request) {
        // 从请求头获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return sysUserService.logout(token);
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/users/current")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的信息")
    public Result<SysUserVO> getCurrentUser(HttpServletRequest request) {
        // 从请求头获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        SysUserVO userVO = sysUserService.getCurrentUserByToken(token);
        return Result.success(userVO);
    }
}