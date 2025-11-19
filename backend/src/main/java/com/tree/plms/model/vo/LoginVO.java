package com.tree.plms.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */

@Data
@ApiModel("登录响应VO")
public class LoginVO {
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("角色名称")
    private String roleName;
    @ApiModelProperty("权限列表")
    private String[] permissions; // 拆分逗号分隔的权限字符串
    @ApiModelProperty("登录令牌")
    private String token;
}