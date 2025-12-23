package com.tree.plms.model.dto.request;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */

// SysUserQueryReq.java（用户查询参数）
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户查询参数")
public class SysUserQueryReq {
    @Schema(description = "用户名", example = "SuohaChan")
    private String username;       // 模糊查询
    @Schema(description = "角色ID", example = "r2024001")
    private String roleId;         // 角色ID
    @Schema(description = "状态", example = "active")
    private String status;         // 状态
    @Schema(description = "页码", example = "1")
    private Long pageNum = 1L;     // 页码
    @Schema(description = "每页条数", example = "10")
    private Long pageSize = 10L;   // 每页条数
}