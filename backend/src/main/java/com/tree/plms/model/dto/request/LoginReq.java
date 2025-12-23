package com.tree.plms.model.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
/**
 * @author SuohaChan
 * @data 2025/11/12
 */
// LoginReq.java（登录请求参数）

@Data
@Schema(description = "登录请求参数")
public class LoginReq {
    @Schema(description = "用户名", example = "admin")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", example = "123456")
    @NotBlank(message = "密码不能为空")
    private String password;
}
