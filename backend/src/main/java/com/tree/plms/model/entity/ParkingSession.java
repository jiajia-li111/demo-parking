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
 * 记录完整停车周期数据
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Getter
@Setter
@TableName("t_parking_session")
@Schema(description = "停车会话")
public class ParkingSession implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会话标识（如"s20241105001"）
     */
    @TableId("session_id")
    @Schema(description = "会话标识")
    private String sessionId;

    /**
     * 关联车辆ID
     */
    @TableField("vehicle_id")
    @Schema(description = "关联车辆ID")
    private String vehicleId;

    /**
     * 关联月卡ID（可选）
     */
    @TableField("card_id")
    @Schema(description = "关联月卡ID")
    private String cardId;

    /**
     * 进场时间（精确到秒）
     */
    @TableField("entry_time")
    @Schema(description = "进场时间")
    private LocalDateTime entryTime;

    /**
     * 出场时间（未离场为null）
     */
    @TableField("exit_time")
    @Schema(description = "出场时间")
    private LocalDateTime exitTime;

    /**
     * 进场通道ID
     */
    @TableField("entry_gate_id")
    @Schema(description = "进场通道ID")
    private String entryGateId;

    /**
     * 出场通道ID（可选）
     */
    @TableField("exit_gate_id")
    @Schema(description = "出场通道ID")
    private String exitGateId;

    /**
     * 占用车位ID（可选）
     */
    @TableField("space_id")
    @Schema(description = "占用车位ID")
    private String spaceId;

    /**
     * 适用规则ID
     */
    @TableField("rule_id")
    @Schema(description = "适用规则ID")
    private String ruleId;
}
