package com.tree.plms.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车辆唯一标识（如"v2024001"）
     */
    @TableId("vehicle_id")
    private String vehicleId;

    /**
     * 车牌号（唯一，如"粤A12345"）
     */
    @TableField("license_plate")
    private String licensePlate;

    /**
     * 车型（01=小型车，02=大型车）
     */
    @TableField("vehicle_type")
    private String vehicleType;

    /**
     * 是否业主车（01=是，02=否）
     */
    @TableField("is_owner_car")
    private String isOwnerCar;

    /**
     * 关联业主ID（仅业主车有值）
     */
    @TableField("owner_id")
    private String ownerId;
}
