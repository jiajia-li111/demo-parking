package com.tree.plms.config;

import com.tree.plms.model.entity.SysUser;
import com.tree.plms.service.SysUserService;
import com.tree.plms.util.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author SuohaChan
 * @data 2025/12/10
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private SysUserService sysUserService;

    @Override

    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException, IOException {

        // 1. 从请求头获取令牌
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        log.info("JWT Token: " + token);

        // 2. 验证令牌并设置认证信息
        if (token != null && jwtUtil.validateToken(token)) {
            String userId = jwtUtil.getUserIdFromToken(token);
            SysUser user = sysUserService.getUserById(userId);

            if (user != null) {
                // 创建认证对象
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, null);

                // 设置认证信息到 SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                log.error("用户不存在");
            }
        }

        // 3. 继续执行过滤器链
        filterChain.doFilter(request, response);
    }
}