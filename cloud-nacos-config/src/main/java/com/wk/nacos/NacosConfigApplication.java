package com.wk.nacos;

import com.wk.nacos.config.UserConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Description : nacos 启动类
 * 配置文件优先级:
 * nacos配置 > application-${profiles}.yaml > application.yaml
 * @Author : wukong
 */
@SpringBootApplication
@EnableConfigurationProperties(UserConfig.class)
public class NacosConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosConfigApplication.class, args);
    }
}
