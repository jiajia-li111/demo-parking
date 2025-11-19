package com.tree.plms.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 存储操作人员信息
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Getter
@Setter
@TableName("t_sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识（如"u202401"）
     */
    @TableId("user_id")
    private String userId;

    /**
     * 登录用户名（唯一）
     */
    @TableField("username")
    private String username;

    /**
     * 加密密码（MD5加密）
     */
    @TableField("password")
    private String password;

    /**
     * 关联角色ID
     */
    @TableField("role_id")
    private String roleId;

    /**
     * 所属部门（固定"管理中心"）
     */
    @TableField("department")
    private String department;

    /**
     * 状态（01=启用，02=禁用）
     */
    @TableField("status")
    private String status;
}
