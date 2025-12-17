package com.tree.plms.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 月度统计数据DTO
 */
@Data
@Schema(description = "月度统计数据")
public class MonthlyStatsDTO {
    
    @Schema(description = "统计月份", example = "2024-11")
    private String month;
    
    @Schema(description = "月均日入场量", example = "95")
    private Integer avgDailyEntry;
    
    @Schema(description = "月均日出活量", example = "90")
    private Integer avgDailyExit;
    
    @Schema(description = "月度总收入", example = "150000.00")
    private BigDecimal monthlyRevenue;
    
    @Schema(description = "每日统计明细")
    private List<DailyStatsDTO> dailyDetails;
}