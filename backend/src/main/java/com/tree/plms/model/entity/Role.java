package com.tree.plms.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色标识（如"r001"）
     */
    @TableId("role_id")
    private String roleId;

    /**
     * 角色名称（如"超级管理员"）
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 权限列表（逗号分隔，如"发卡,计费"）
     */
    @TableField("permissions")
    private String permissions;
}
