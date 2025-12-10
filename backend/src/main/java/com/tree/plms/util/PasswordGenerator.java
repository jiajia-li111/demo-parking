package com.tree.plms.util;

/**
 * @author SuohaChan
 * @data 2025/12/10
 */

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成工具类，用于生成BCrypt加密密码
 */
public class PasswordGenerator {
    public static void main(String[] args) {
        // 创建BCrypt密码加密器
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 原始密码
        String rawPassword = "123456";

        // 生成加密后的密码
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 打印加密后的密码
        System.out.println("原始密码: " + rawPassword);
        System.out.println("BCrypt加密密码: " + encodedPassword);

        // 验证密码（可选）
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("密码验证结果: " + matches);
    }
}