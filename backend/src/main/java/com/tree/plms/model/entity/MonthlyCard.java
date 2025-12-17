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
 * 存储业主车月卡信息
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Getter
@Setter
@TableName("t_monthly_card")
@Schema(description = "月卡")
public class MonthlyCard implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 月卡唯一标识（如"c2024001"）
     */
    @TableId("card_id")
    @Schema(description = "月卡唯一标识")
    private String cardId;

    /**
     * 绑定车辆ID
     */
    @TableField("vehicle_id")
    @Schema(description = "绑定车辆ID")
    private String vehicleId;

    /**
     * 发卡人ID
     */
    @TableField("issuer_id")
    @Schema(description = "发卡人ID")
    private String issuerId;

    /**
     * 生效时间（如"2024-01-01 00:00"）
     */
    @TableField("start_date")
    @Schema(description = "生效时间")
    private LocalDateTime startDate;

    /**
     * 到期时间（如"2024-12-31 23:59"）
     */
    @TableField("end_date")
    @Schema(description = "到期时间")
    private LocalDateTime endDate;

    /**
     * 状态（01=启用，02=挂失，03=过期）
     */
    @TableField("status")
    @Schema(description = "状态")
    private String status;
}
