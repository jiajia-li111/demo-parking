package com.tree.plms.model.vo;

/**
 * @author SuohaChan
 * @data 2025/12/22
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 过闸事件VO（包含车辆车牌号）
 */
@Data
@Schema(description = "过闸事件信息VO")
public class AccessEventVO {

    @Schema(description = "事件标识")
    private String eventId;

    @Schema(description = "通道ID")
    private String gateId;

    @Schema(description = "事件时间")
    private LocalDateTime eventTime;

    @Schema(description = "关联车辆ID")
    private String vehicleId;

    @Schema(description = "车辆车牌号")
    private String licensePlate;

    @Schema(description = "识别结果（01=成功，02=无牌，03=黑名单）")
    private String recognitionResult;

    @Schema(description = "事件类型（01=进场，02=出场）")
    private String eventType;

    @Schema(description = "关联会话ID")
    private String sessionId;

    @Schema(description = "处理状态（01=放行，02=拦截，03=人工操作）")
    private String handleStatus;

    @Schema(description = "操作人ID")
    private String operatorId;
}
