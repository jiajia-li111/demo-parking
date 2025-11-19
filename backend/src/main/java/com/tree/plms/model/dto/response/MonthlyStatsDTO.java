package com.tree.plms.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 月度统计数据DTO
 */
@Data
@ApiModel(description = "月度统计数据")
public class MonthlyStatsDTO {
    
    @ApiModelProperty(value = "统计月份", example = "2024-11")
    private String month;
    
    @ApiModelProperty(value = "月均日入场量", example = "95")
    private Integer avgDailyEntry;
    
    @ApiModelProperty(value = "月均日出活量", example = "90")
    private Integer avgDailyExit;
    
    @ApiModelProperty(value = "月度总收入", example = "150000.00")
    private BigDecimal monthlyRevenue;
    
    @ApiModelProperty(value = "每日统计明细")
    private List<DailyStatsDTO> dailyDetails;
}