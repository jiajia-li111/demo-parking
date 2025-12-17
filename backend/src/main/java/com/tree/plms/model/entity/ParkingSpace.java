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
 * 存储单个车位信息
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Getter
@Setter
@TableName("t_parking_space")
@Schema(name = "ParkingSpace", description = "存储单个车位信息")
public class ParkingSpace implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车位唯一标识（如"b1_001"）
     */
    @TableId("space_id")
    @Schema(description = "车位唯一标识（如\"b1_001\"）")
    private String spaceId;

    /**
     * 所属楼层ID
     */
    @TableField("floor_id")
    @Schema(description = "所属楼层ID")
    private String floorId;

    /**
     * 车位编号（楼层内编号，如"001"）
     */
    @TableField("space_no")
    @Schema(description = "车位编号（楼层内编号，如\"001\"）")
    private String spaceNo;

    /**
     * 状态（01=空闲，02=占用，03=故障）
     */
    @TableField("status")
    @Schema(description = "状态（01=空闲，02=占用，03=故障）")
    private String status;

    /**
     * 是否固定车位（01=是，02=否）
     */
    @TableField("is_fixed")
    @Schema(description = "是否固定车位（01=是，02=否）")
    private String isFixed;

    /**
     * 关联业主ID（仅固定车位有值）
     */
    @TableField("owner_id")
    @Schema(description = "关联业主ID（仅固定车位有值）")
    private String ownerId;
}
