package com.wk.nacos;

import com.wk.nacos.model.UserConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Description :
 * @Author : wukong
 * @Date : 2022/7/6 11:53 下午
 */
@SpringBootApplication
@EnableConfigurationProperties(UserConfig.class)
public class NacosApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosApplication.class, args);
    }
}
