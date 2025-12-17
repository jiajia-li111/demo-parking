package com.tree.plms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI parkingLotAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PLMS Parking System API")
                        .version("1.0.0")  // 使用数字版本号格式
                        .description("停车场管理系统API文档")
                );
    }
}