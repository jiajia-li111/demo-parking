package com.tree.plms.config;

import com.tree.plms.config.JwtAuthenticationFilter;  // 添加这行导入
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 安全配置类（提供密码加密器和安全规则配置）
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 密码加密器（BCrypt算法，自动加盐）
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // 允许所有请求访问
                        .anyRequest().permitAll()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);
        return http.build();
    }

//    /**
//     * 安全过滤器链配置
//     */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http,
//                                                JwtAuthenticationFilter jwtFilter) throws Exception {
//        http
//            // 禁用CSRF保护，便于开发环境测试
//            .csrf(AbstractHttpConfigurer::disable)
//            // 配置请求授权规则
//            .authorizeHttpRequests(authorize -> authorize
//                // 允许登录接口公开访问
//                .requestMatchers("/auth/login").permitAll()
//                // 允许Swagger接口文档相关路径公开访问
//                .requestMatchers(
//                    "/swagger-ui.html",
//                        // 精确匹配Swagger UI首页
//                        "/v3/api-docs.yaml",
//                    "/swagger-ui/**",         // 匹配Swagger UI的所有资源
//                    "/v3/api-docs/**",        // 匹配API文档JSON
//                    "/swagger-resources/**",  // 匹配Swagger资源
//                    "/api-docs/**",           // 匹配旧版API文档路径
//                    "/webjars/**"             // 匹配Swagger UI使用的WebJars资源
//                ).permitAll()
//                // 其他所有请求需要认证
//                .anyRequest().authenticated()
//            )
//            // 禁用表单登录，使用自定义的登录接口
//            .formLogin(AbstractHttpConfigurer::disable)
//            // 禁用HTTP基本认证
//            .httpBasic(AbstractHttpConfigurer::disable)
//             // 添加 JWT 过滤器
//             .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
}