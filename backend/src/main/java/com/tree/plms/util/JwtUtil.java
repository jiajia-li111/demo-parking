package com.tree.plms.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类（生成/解析令牌）
 */
@Component
public class JwtUtil {

    // 令牌过期时间（3小时，单位：毫秒）
    @Value("${jwt.expiration:10800000}")
    private long expiration;

    // 签名密钥（实际项目中放在配置文件，且长度不少于256位）
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * 生成令牌（包含用户ID）
     */
    public String generateToken(String userId) {
        // 过期时间：当前时间 + 过期时长
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);

        // 构建令牌（包含用户ID作为载荷）
        return Jwts.builder()
                .setClaims(new HashMap<String, Object>() {{
                    put("userId", userId); // 自定义载荷：用户ID
                }})
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(expirationDate) // 过期时间
                .signWith(secretKey) // 签名
                .compact();
    }

    /**
     * 从令牌中解析用户ID
     */
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", String.class);
    }

    /**
     * 验证令牌是否有效（未过期且签名正确）
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 令牌无效（过期、签名错误等）
            return false;
        }
    }
}