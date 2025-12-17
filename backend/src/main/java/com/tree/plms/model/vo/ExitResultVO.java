package com.tree.plms.model.vo;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 车辆出场结果VO
 */
@Data
@Schema(description = "车辆出场结果信息")
public class ExitResultVO {

    @Schema(description = "停车会话ID（关联本次停车记录）", example = "s20241105001")
    private String sessionId;

    @Schema(description = "停车时长（单位：小时，保留1位小数）", example = "3.5")
    private Double parkingHours;

    @Schema(description = "应付金额（单位：元）", example = "11.00")
    private BigDecimal payAmount;

    @Schema(description = "支付方式（01=微信，02=支付宝，03=现金）", example = "01")
    private String payMethod;

    @Schema(description = "支付方式名称", example = "微信支付")
    private String payMethodName;

    @Schema(description = "支付状态（01=成功，02=失败，03=退款）", example = "01")
    private String payStatus;

    @Schema(description = "支付状态描述", example = "支付成功")
    private String payStatusDesc;

    @Schema(description = "支付时间（yyyy-MM-dd HH:mm:ss）", example = "2024-11-05 12:30:00")
    private String payTime;

    @Schema(description = "出场时间（yyyy-MM-dd HH:mm:ss）", example = "2024-11-05 12:30:00")
    private String exitTime;

    @Schema(description = "是否放行（true=放行，false=拦截）", example = "true")
    private Boolean pass;

    @Schema(description = "提示信息", example = "出场成功，感谢使用")
    private String message;

    // 格式化时间（便于前端直接展示）
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public void setPayTime(LocalDateTime payTime) {
        this.payTime = payTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}