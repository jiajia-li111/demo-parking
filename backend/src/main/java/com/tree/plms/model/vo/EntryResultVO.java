package com.tree.plms.model.vo;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 车辆进场结果VO
 */
@Data
@Schema(description = "车辆进场结果信息")
public class EntryResultVO {

    @Schema(description = "停车会话ID（唯一标识本次停车）", example = "s20241105001")
    private String sessionId;

    @Schema(description = "分配的车位ID", example = "b1_001")
    private String spaceId;

    @Schema(description = "是否放行（true=放行，false=拦截）", example = "true")
    private Boolean pass;

    @Schema(description = "车位所在楼层", example = "地下一层")
    private String floorName;

    @Schema(description = "进场时间（yyyy-MM-dd HH:mm:ss）", example = "2024-11-05 08:30:00")
    private String entryTime;

    @Schema(description = "提示信息（如无车位时的原因）", example = "进场成功，已分配车位")
    private String message;
}