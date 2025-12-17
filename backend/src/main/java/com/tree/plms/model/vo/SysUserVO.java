package com.tree.plms.model.vo;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */
// 用户详情VO（前端展示用）

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户详情VO")
public class SysUserVO {
    @Schema(description = "用户ID")
    private String userId;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "角色ID")
    private String roleId;
    @Schema(description = "角色名称")
    private String roleName;
    @Schema(description = "部门")
    private String department;
    @Schema(description = "状态（01=启用，02=禁用）")
    private String status;
    @Schema(description = "状态描述")
    private String statusDesc; // 由状态码转换（如"启用"）
}


