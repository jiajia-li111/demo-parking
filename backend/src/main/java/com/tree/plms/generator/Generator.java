package com.tree.plms.generator;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;

public class Generator {
    public static void main(String[] args) {
        // 1. 数据库配置（修改为你的本地MySQL信息）
        String url = "jdbc:mysql://localhost:3306/PLMS?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
        String username = "root"; // 你的MySQL用户名
        String password = "ljj21041102"; // 你的MySQL密码

        // 2. 代码生成配置
        FastAutoGenerator.create(url, username, password)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("tree") // 作者名
                            .outputDir(System.getProperty("user.dir") + "/src/main/java") // 输出目录
                            .commentDate("yyyy-MM-dd") // 注释日期
                            .disableOpenDir(); // 生成后不打开文件夹
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.parent("com.plms") // 父包名
                            .entity("model.entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .xml("mybatis")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mybatis"));
                })
                // 策略配置（核心：去除表前缀 t_）
                .strategyConfig(builder -> {
                    builder.addInclude("t_floor", "t_parking_space", "t_gate", "t_owner", "t_vehicle",
                                    "t_sys_user", "t_role", "t_monthly_card", "t_fee_rule", "t_parking_session",
                                    "t_access_event", "t_payment") // 所有表名
                            .addTablePrefix("t_") // 去除表前缀 t_（关键配置）
                            .entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .columnNaming(com.baomidou.mybatisplus.generator.config.rules.NamingStrategy.underline_to_camel)
                            .mapperBuilder()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .controllerBuilder()
                            .enableRestStyle()
                            .enableHyphenStyle()
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl");
                })
                .templateEngine(new VelocityTemplateEngine())
                .execute();
    }
}