package com.wk.sharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description : sharding application
 * @Author : wukong
 * @Date : 2022/7/5 10:30 下午
 */
@SpringBootApplication
@MapperScan({ "com.wk.**.mapper.**" })
public class ShardingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingApplication.class, args);
    }
}
