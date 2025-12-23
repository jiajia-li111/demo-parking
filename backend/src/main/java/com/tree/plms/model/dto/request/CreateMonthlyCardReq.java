package com.tree.plms.model.dto.request;

/**
 * @author SuohaChan
 * @data 2025/12/23
 */

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "创建月度卡片请求参数")
public class CreateMonthlyCardReq {
    @Schema(description = "车牌号", example = "粤B12345")
    @NotBlank(message = "车牌号不能为空")
    private String licensePlate;

    @Schema(description = "发卡人ID")
    @NotBlank(message = "发卡人ID不能为空")
    private String issuerId;

    @TableField("end_date")
    @Schema(description = "到期时间")
    private LocalDateTime endDate;
}
