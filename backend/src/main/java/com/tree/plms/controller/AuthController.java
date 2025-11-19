package com.tree.plms.controller;

import com.tree.plms.model.dto.request.LoginReq;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.vo.LoginVO;
import com.tree.plms.model.vo.SysUserVO;
import com.tree.plms.service.SysUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器（处理登录登出）
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginReq req) {
        LoginVO loginVO = sysUserService.login(req);
        return Result.success(loginVO);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
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