package com.mikou.edgecloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.mikou.edgecloud.*.infrastructure.persistence.mapper", "com.mikou.edgecloud.*.domain.infrastructure.persistence.mapper"})
public class EdgecloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(EdgecloudApplication.class, args);
    }
}
