package com.tree.plms.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 日常统计数据DTO
 */
@Data
@ApiModel(description = "日常统计数据")
public class DailyStatsDTO {
    
    @ApiModelProperty(value = "统计日期", example = "2024-11-05")
    private String date;
    
    @ApiModelProperty(value = "入场车辆数", example = "100")
    private Integer entryCount;
    
    @ApiModelProperty(value = "出场车辆数", example = "95")
    private Integer exitCount;
    
    @ApiModelProperty(value = "当日收入", example = "5000.00")
    private BigDecimal totalRevenue;
    
    @ApiModelProperty(value = "平均停车时长(分钟)", example = "120")
    private Integer avgParkingMinutes;
}