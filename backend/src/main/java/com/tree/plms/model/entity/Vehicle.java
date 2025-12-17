package com.tree.plms.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 存储车辆基本信息
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Getter
@Setter
@TableName("t_vehicle")
@Schema(description = "车辆")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车辆唯一标识（如"v2024001"）
     */
    @TableId("vehicle_id")
    @Schema(description = "车辆唯一标识")
    private String vehicleId;

    /**
     * 车牌号（唯一，如"粤A12345"）
     */
    @TableField("license_plate")
    @Schema(description = "车牌号")
    private String licensePlate;

    /**
     * 车型（01=小型车，02=大型车）
     */
    @TableField("vehicle_type")
    @Schema(description = "车型")
    private String vehicleType;

    /**
     * 是否业主车（01=是，02=否）
     */
    @TableField("is_owner_car")
    @Schema(description = "是否业主车")
    private String isOwnerCar;

    /**
     * 关联业主ID（仅业主车有值）
     */
    @TableField("owner_id")
    @Schema(description = "关联业主ID")
    private String ownerId;

    /**
     * 是否在停车场内（01=是，02=否）
     */
    @TableField("is_parking")
    @Schema(description = "是否在停车场内")
    private String isParking;
}
