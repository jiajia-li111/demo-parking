package com.tree.plms.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */

@Data
@Schema(description = "登录响应VO")
public class LoginVO {
    @Schema(description = "用户ID")
    private String userId;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "角色名称")
    private String roleName;
    @Schema(description = "权限列表")
    private String[] permissions; // 拆分逗号分隔的权限字符串
    @Schema(description = "登录令牌")
    private String token;
}