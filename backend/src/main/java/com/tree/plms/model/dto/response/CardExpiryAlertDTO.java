package com.tree.plms.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 月卡到期提醒DTO
 */
@Data
@ApiModel(description = "月卡到期提醒")
public class CardExpiryAlertDTO {
    
    @ApiModelProperty(value = "月卡ID", example = "c2024001")
    private String cardId;
    
    @ApiModelProperty(value = "车辆ID", example = "v2024001")
    private String vehicleId;
    
    @ApiModelProperty(value = "车牌号", example = "京A12345")
    private String licensePlate;
    
    @ApiModelProperty(value = "到期时间")
    private LocalDateTime endDate;
    
    @ApiModelProperty(value = "剩余天数", example = "7")
    private Integer remainingDays;
}
