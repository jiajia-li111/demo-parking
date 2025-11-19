package com.tree.plms.config;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全配置类（提供密码加密器）
 */
@Configuration
public class SecurityConfig {

    /**
     * 密码加密器（BCrypt算法，自动加盐）
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}