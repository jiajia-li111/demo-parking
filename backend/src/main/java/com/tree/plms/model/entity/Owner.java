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
 * 存储小区业主信息
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Getter
@Setter
@TableName("t_owner")
@Schema(description = "业主")
public class Owner implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业主唯一标识（如"o202401"）
     */
    @TableId("owner_id")
    @Schema(description = "业主唯一标识")
    private String ownerId;

    /**
     * 业主姓名
     */
    @TableField("name")
    @Schema(description = "业主姓名")
    private String name;

    /**
     * 房号（如"3栋2单元501"）
     */
    @TableField("room_no")
    @Schema(description = "房号")
    private String roomNo;

    /**
     * 联系电话（唯一）
     */
    @TableField("phone")
    @Schema(description = "联系电话")
    private String phone;
}
