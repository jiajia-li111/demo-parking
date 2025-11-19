package com.tree.plms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tree.plms.mapper")
public class PlmsApplication {

    public static void main(String[] args) {

        SpringApplication.run(PlmsApplication.class, args);
    }

}
