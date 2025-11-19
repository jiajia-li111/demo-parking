package com.tree.plms.model.dto.request;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */

// SysUserQueryReq.java（用户查询参数）
import lombok.Data;

@Data
public class SysUserQueryReq {
    private String username;       // 模糊查询
    private String roleId;         // 角色ID
    private String status;         // 状态
    private Long pageNum = 1L;     // 页码
    private Long pageSize = 10L;   // 每页条数
}