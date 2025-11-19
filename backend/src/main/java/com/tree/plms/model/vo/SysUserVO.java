package com.tree.plms.model.vo;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */
// 用户详情VO（前端展示用）
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户详情VO")
public class SysUserVO {
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("角色ID")
    private String roleId;
    @ApiModelProperty("角色名称")
    private String roleName;
    @ApiModelProperty("部门")
    private String department;
    @ApiModelProperty("状态（01=启用，02=禁用）")
    private String status;
    @ApiModelProperty("状态描述")
    private String statusDesc; // 由状态码转换（如"启用"）
}


