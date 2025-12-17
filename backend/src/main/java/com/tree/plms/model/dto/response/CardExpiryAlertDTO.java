package com.tree.plms.model.dto.response;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 月卡到期提醒DTO
 */
@Data
@Schema(description = "月卡到期提醒")
public class CardExpiryAlertDTO {
    
    @Schema(description = "月卡ID", example = "c2024001")
    private String cardId;
    
    @Schema(description = "车辆ID", example = "v2024001")
    private String vehicleId;
    
    @Schema(description = "车牌号", example = "京A12345")
    private String licensePlate;
    
    @Schema(description = "到期时间")
    private LocalDateTime endDate;
    
    @Schema(description = "剩余天数", example = "7")
    private Integer remainingDays;
}
