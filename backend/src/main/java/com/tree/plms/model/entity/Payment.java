package com.tree.plms.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 存储停车缴费详情
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Getter
@Setter
@TableName("t_payment")
@Schema(name = "Payment", description = "存储停车缴费详情")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付记录标识（如"p20241105001"）
     */
    @TableId("payment_id")
    @Schema(description = "支付记录标识（如\"p20241105001\"）")
    private String paymentId;

    /**
     * 关联会话ID
     */
    @TableField("session_id")
    @Schema(description = "关联会话ID")
    private String sessionId;

    /**
     * 支付金额（单位：元）
     */
    @TableField("amount")
    @Schema(description = "支付金额（单位：元）")
    private BigDecimal amount;

    /**
     * 支付方式（01=微信，02=支付宝，03=现金）
     */
    @TableField("pay_method")
    @Schema(description = "支付方式（01=微信，02=支付宝，03=现金）")
    private String payMethod;

    /**
     * 支付时间（精确到秒）
     */
    @TableField("pay_time")
    @Schema(description = "支付时间（精确到秒）")
    private LocalDateTime payTime;

    /**
     * 支付状态（01=成功，02=失败，03=退款）
     */
    @TableField("status")
    @Schema(description = "支付状态（01=成功，02=失败，03=退款）")
    private String status;

    /**
     * 第三方支付单号（如微信支付流水号）
     */
    @TableField("transaction_id")
    @Schema(description = "第三方支付单号（如微信支付流水号）")
    private String transactionId;
}
