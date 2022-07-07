package com.wk.nacos.controller;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.NacosConfigService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wk.nacos.model.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description :
 * @Author : wukong
 * @Date : 2022/7/6 11:38 下午
 */
@RestController
public class UserController {
    @Autowired
    private UserConfig userConfig;

    @GetMapping
    public String get() throws JsonProcessingException, NacosException {
        return new ObjectMapper().writeValueAsString(userConfig);
    }
}
