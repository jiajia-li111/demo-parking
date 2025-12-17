package com.tree.plms.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 存储角色及权限信息
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Getter
@Setter
@TableName("t_role")
@Schema(name = "Role", description = "存储角色及权限信息")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色标识（如"r001"）
     */
    @TableId("role_id")
    @Schema(name = "roleId", description = "角色标识（如\"r001\"）")
    private String roleId;

    /**
     * 角色名称（如"超级管理员"）
     */
    @TableField("role_name")
    @Schema(name = "roleName", description = "角色名称（如\"超级管理员\"）")
    private String roleName;

    /**
     * 权限列表（逗号分隔，如"发卡,计费"）
     */
    @TableField("permissions")
    @Schema(name = "permissions", description = "权限列表（逗号分隔，如\"发卡,计费\"）")
    private String permissions;
}
