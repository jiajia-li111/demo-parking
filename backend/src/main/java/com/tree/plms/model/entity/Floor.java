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
 * 存储停车场楼层信息
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Getter
@Setter
@TableName("t_floor")
@Schema(description = "plms停车场楼层")
public class Floor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 楼层唯一标识（如"b1"）
     */
    @TableId("floor_id")
    @Schema(description = "楼层唯一标识")
    private String floorId;

    /**
     * 楼层名称（如"地下一层"）
     */
    @TableField("floor_name")
    @Schema(description = "楼层名称")
    private String floorName;

    /**
     * 总车位数（每层固定200个车位）
     */
    @Schema(description = "总车位数")
    @TableField("total_spaces")
    private Integer totalSpaces;
}
