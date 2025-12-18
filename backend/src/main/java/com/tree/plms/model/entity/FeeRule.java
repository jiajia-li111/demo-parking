package com.tree.plms.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 存储计费标准
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Getter
@Setter
@TableName("t_fee_rule")
@Schema(description = "plms计费标准")
public class FeeRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则标识（如"r00001"）
     */
    @TableId("rule_id")
    @Schema(description = "规则标识")
    private String ruleId;

    /**
     * 规则名称（如"临时车白天费率"）
     */
    @TableField("rule_name")
    @Schema(description = "规则名称")
    private String ruleName;

    /**
     * 适用对象（01=临时车，02=月卡超时）
     */
    @TableField("apply_to")
    @Schema(description = "适用对象")
    private String applyTo;

    /**
     * 适用车型（01=小型车，02=大型车）
     */
    @TableField("vehicle_type")
    @Schema(description = "适用车型")
    private String vehicleType;

    /**
     * 首小时费用（单位：元）
     */
    @TableField("first_hour_fee")
    @Schema(description = "首小时费用")
    private BigDecimal firstHourFee;

    /**
     * 后续小时费用（单位：元）
     */
    @TableField("next_hour_fee")
    @Schema(description = "后续小时费用")
    private BigDecimal nextHourFee;

    /**
     * 每日封顶费用（单位：元）
     */
    @TableField("daily_cap")
    @Schema(description = "每日封顶费用")
    private BigDecimal dailyCap;

    /**
     * 夜间时段开始（如"20:00"）
     */
    @TableField("night_start")
    @Schema(description = "夜间时段开始")
    private String nightStart;

    /**
     * 夜间时段结束（如"08:00"）
     */
    @TableField("night_end")
    @Schema(description = "夜间时段结束")
    private String nightEnd;

    /**
     * 夜间费率（单位：元/小时）
     */
    @TableField("night_rate")
    @Schema(description = "夜间费率")
    private BigDecimal nightRate;


    /**
     * 状态（01=有效，02=无效）
     */
    @TableField("status")
    @Schema(description = "状态")
    private String status;

}
