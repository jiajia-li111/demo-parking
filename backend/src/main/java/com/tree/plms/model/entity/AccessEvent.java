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
 * 记录车辆过闸行为
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Getter
@Setter
@TableName("t_access_event")
@Schema(name = "AccessEvent", description = "记录车辆过闸行为")
public class AccessEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件标识（如"e20241105001"）
     */
    @TableId("event_id")
    @Schema(description = "事件标识（如\"e20241105001\"）")
    private String eventId;

    /**
     * 通道ID
     */
    @TableField("gate_id")
    @Schema(description = "通道ID")
    private String gateId;

    /**
     * 事件时间（精确到秒）
     */
    @TableField("event_time")
    @Schema(description = "事件时间（精确到秒）")
    private LocalDateTime eventTime;

    /**
     * 关联车辆ID（未识别为null）
     */
    @TableField("vehicle_id")
    @Schema(description = "关联车辆ID（未识别为null）")
    private String vehicleId;

    /**
     * 识别结果（01=成功，02=无牌，03=黑名单）
     */
    @TableField("recognition_result")
    @Schema(description = "识别结果（01=成功，02=无牌，03=黑名单）")
    private String recognitionResult;

    /**
     * 事件类型（01=进场，02=出场）
     */
    @TableField("event_type")
    @Schema(description = "事件类型（01=进场，02=出场）")
    private String eventType;

    /**
     * 关联会话ID（出场时必填）
     */
    @TableField("session_id")
    @Schema(description = "关联会话ID（出场时必填）")
    private String sessionId;

    /**
     * 处理状态（01=放行，02=拦截，03=人工操作）
     */
    @TableField("handle_status")
    @Schema(description = "处理状态（01=放行，02=拦截，03=人工操作）")
    private String handleStatus;

    /**
     * 操作人ID（人工操作时必填）
     */
    @TableField("operator_id")
    @Schema(description = "操作人ID（人工操作时必填）")
    private String operatorId;
}
