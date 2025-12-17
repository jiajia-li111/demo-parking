package com.tree.plms.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 日常统计数据DTO
 */
@Data
@Schema(description = "日常统计数据")
public class DailyStatsDTO {
    
    @Schema(description = "统计日期", example = "2024-11-05")
    private String date;
    
    @Schema(description = "入场车辆数", example = "100")
    private Integer entryCount;
    
    @Schema(description = "出场车辆数", example = "95")
    private Integer exitCount;
    
    @Schema(description = "当日收入", example = "5000.00")
    private BigDecimal totalRevenue;
    
    @Schema(description = "平均停车时长(分钟)", example = "120")
    private Integer avgParkingMinutes;
}