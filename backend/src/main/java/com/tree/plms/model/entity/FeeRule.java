package com.tree.plms.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class FeeRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则标识（如"r00001"）
     */
    @TableId("rule_id")
    private String ruleId;

    /**
     * 规则名称（如"临时车白天费率"）
     */
    @TableField("rule_name")
    private String ruleName;

    /**
     * 适用对象（01=临时车，02=月卡超时）
     */
    @TableField("apply_to")
    private String applyTo;

    /**
     * 适用车型（01=小型车，02=大型车）
     */
    @TableField("vehicle_type")
    private String vehicleType;

    /**
     * 首小时费用（单位：元）
     */
    @TableField("first_hour_fee")
    private BigDecimal firstHourFee;

    /**
     * 后续小时费用（单位：元）
     */
    @TableField("next_hour_fee")
    private BigDecimal nextHourFee;

    /**
     * 每日封顶费用（单位：元）
     */
    @TableField("daily_cap")
    private BigDecimal dailyCap;

    /**
     * 夜间时段开始（如"20:00"）
     */
    @TableField("night_start")
    private String nightStart;

    /**
     * 夜间时段结束（如"08:00"）
     */
    @TableField("night_end")
    private String nightEnd;

    /**
     * 夜间费率（单位：元/小时）
     */
    @TableField("night_rate")
    private BigDecimal nightRate;

    /**
     * 生效日期（如"2024-01-01"）
     */
    @TableField("effective_date")
    private LocalDate effectiveDate;
}
