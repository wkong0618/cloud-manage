package com.wk.nacos.utils;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;

/**
 * @Description : nacos config
 * @Author : wukong
 */
public class NacosConfigUtil {

    public static void main(String[] args) throws Exception {
        String serverAddr = "127.0.0.1:8848";
        String dataId = "example.yml";
        String group = "DEFAULT_GROUP";
        ConfigService configService = NacosFactory.createConfigService(serverAddr);
        String yamlConfigStr = configService.getConfig(dataId, group, 5000);
    }
}
